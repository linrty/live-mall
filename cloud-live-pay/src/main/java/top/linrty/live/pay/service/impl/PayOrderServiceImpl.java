package top.linrty.live.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import top.linrty.live.common.utils.RedisSeqIdHelper;
import top.linrty.live.pay.domain.po.PayOrder;
import top.linrty.live.pay.mapper.PayOrderMapper;
import top.linrty.live.pay.service.IPayOrderService;
import top.linrty.live.pay.utils.PayProviderCacheKeyBuilder;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 1:59
 * @Version: 1.0
 **/
@Service
public class PayOrderServiceImpl implements IPayOrderService {

    @Resource
    private PayOrderMapper payOrderMapper;
    @Resource
    private RedisSeqIdHelper redisSeqIdHelper;

    @Resource
    private PayProviderCacheKeyBuilder payProviderCacheKeyBuilder;


    @Override
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
}
