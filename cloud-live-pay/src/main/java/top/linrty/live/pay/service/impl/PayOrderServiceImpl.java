package top.linrty.live.pay.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import top.linrty.live.common.domain.dto.pay.PayOrderDTO;
import top.linrty.live.common.domain.dto.pay.PayProductDTO;
import top.linrty.live.common.enums.pay.OrderStatusEnum;
import top.linrty.live.common.enums.pay.PayProductTypeEnum;
import top.linrty.live.common.utils.RedisSeqIdHelper;
import top.linrty.live.pay.domain.po.PayOrder;
import top.linrty.live.pay.domain.po.PayTopic;
import top.linrty.live.pay.mapper.PayOrderMapper;
import top.linrty.live.pay.service.*;
import top.linrty.live.pay.utils.PayProviderCacheKeyBuilder;

import java.util.concurrent.CompletableFuture;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 1:59
 * @Version: 1.0
 **/
@Service
@Slf4j
public class PayOrderServiceImpl implements IPayOrderService {

    @Resource
    private PayOrderMapper payOrderMapper;
    @Resource
    private RedisSeqIdHelper redisSeqIdHelper;

    @Resource
    private PayProviderCacheKeyBuilder payProviderCacheKeyBuilder;

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Resource
    private ICurrencyAccountService currencyAccountService;

    @Resource
    private IPayTopicService payTopicService;

    @Resource
    private IPayProductService payProductService;


    @Override
    @DS("read_db")
    public PayOrder queryByOrderId(String orderId) {
        LambdaQueryWrapper<PayOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PayOrder::getOrderId, orderId);
        queryWrapper.last("limit 1");
        return payOrderMapper.selectOne(queryWrapper);
    }

    @Override
    public String insertOne(PayOrder payOrderPO) {
        String orderId = String.valueOf(redisSeqIdHelper.nextId(payProviderCacheKeyBuilder.buildPayOrderIdIncrKey()));
        payOrderPO.setOrderId(orderId);
        payOrderMapper.insert(payOrderPO);
        return payOrderPO.getOrderId();
    }

    @Override
    public boolean updateOrderStatus(Long id, Integer status) {
        PayOrder payOrder = new PayOrder();
        payOrder.setId(id);
        payOrder.setStatus(status);
        return payOrderMapper.updateById(payOrder) > 0;
    }

    @Override
    public boolean updateOrderStatus(String orderId, Integer status) {
        PayOrder payOrder = new PayOrder();
        payOrder.setStatus(status);
        LambdaUpdateWrapper<PayOrder> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(PayOrder::getOrderId, orderId);
        return payOrderMapper.update(payOrder, updateWrapper) > 0;
    }

    @Override
    public boolean payNotify(PayOrderDTO payOrderDTO) {
        // bizCode 与 order 校验
        PayOrder payOrder = this.queryByOrderId(payOrderDTO.getOrderId());
        if (payOrder == null) {
            log.error("[PayOrderServiceImpl] payOrderPO is null, create a payOrderPO, userId is {}", payOrderDTO.getUserId());
            currencyAccountService.insertOne(payOrderDTO.getUserId());
            payOrder = this.queryByOrderId(payOrderDTO.getOrderId());
        }
        PayTopic payTopic = payTopicService.getByCode(payOrderDTO.getBizCode());
        if (payTopic == null || StrUtil.isEmpty(payTopic.getTopic())) {
            log.error("[PayOrderServiceImpl] error payTopicPO, payTopicPO is {}", payOrderDTO);
            return false;
        }
        // 调用bank层相应的一些操作
        payNotifyHandler(payOrder);

        // 支付成功后：根据bizCode发送mq 异步通知对应的关心的 服务
        CompletableFuture<SendResult<String, String>> sendResult = kafkaTemplate.send(payTopic.getTopic(), JSON.toJSONString(payOrder));
        sendResult.whenComplete((v, e) -> {
            if (e == null) {
                log.info("[PayOrderServiceImpl] payNotify: send success, orderId is {}", payOrderDTO.getOrderId());
            }
        }).exceptionally(e -> {
            log.error("[PayOrderServiceImpl] payNotify: send failed, orderId is {}", payOrderDTO.getOrderId());
            return null;
        });
        return true;
    }

    /**
     * 在bank层处理一些操作：
     * 如 判断充值商品类型，去做对应的商品记录（如：购买虚拟币，进行余额增加，和流水记录）
     */
    private void payNotifyHandler(PayOrder payOrder) {
        // 更新订单状态为已支付
        this.updateOrderStatus(payOrder.getOrderId(), OrderStatusEnum.PAYED.getCode());
        Integer productId = payOrder.getProductId();
        PayProductDTO payProductDTO = payProductService.getByProductId(productId);
        if (payProductDTO != null && payProductDTO.getType().equals(PayProductTypeEnum.LIVE_COIN.getCode())) {
            // 类型是充值虚拟币业务：
            Long userId = payOrder.getUserId();
            JSONObject jsonObject = JSON.parseObject(payProductDTO.getExtra());
            Integer coinNum = jsonObject.getInteger("coin");
            currencyAccountService.incr(userId, coinNum);
        }
    }
}
