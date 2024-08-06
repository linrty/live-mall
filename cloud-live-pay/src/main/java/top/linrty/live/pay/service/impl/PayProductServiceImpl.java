package top.linrty.live.pay.service.impl;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.linrty.live.common.config.redis.RedisKeyTime;
import top.linrty.live.common.constants.HTTPKeyConstants;
import top.linrty.live.common.domain.dto.pay.PayOrderDTO;
import top.linrty.live.common.domain.dto.pay.PayProductDTO;
import top.linrty.live.common.domain.dto.pay.PayProductReqDTO;
import top.linrty.live.common.domain.vo.pay.PayProductRespVO;
import top.linrty.live.common.enums.StatusEnum;
import top.linrty.live.common.enums.pay.OrderStatusEnum;
import top.linrty.live.common.enums.pay.PaySourceEnum;
import top.linrty.live.common.exception.ParamException;
import top.linrty.live.common.utils.ConvertBeanUtils;
import top.linrty.live.common.utils.RequestContext;
import top.linrty.live.pay.domain.po.PayOrder;
import top.linrty.live.pay.domain.po.PayProduct;
import top.linrty.live.pay.mapper.PayProductMapper;
import top.linrty.live.pay.service.IPayOrderService;
import top.linrty.live.pay.service.IPayProductService;
import top.linrty.live.pay.utils.PayProviderCacheKeyBuilder;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 1:26
 * @Version: 1.0
 **/
@Service
public class PayProductServiceImpl implements IPayProductService {
    @Resource
    private PayProductMapper payProductMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private PayProviderCacheKeyBuilder payProviderCacheKeyBuilder;

    @Resource
    private IPayOrderService payOrderService;
    @Override
    public List<PayProductDTO> products(Integer type) {
        String cacheKey = payProviderCacheKeyBuilder.buildPayProductCache(type);
        List<PayProductDTO> cacheList = redisTemplate.opsForList().range(cacheKey, 0, 30).stream().map(x -> (PayProductDTO) x).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(cacheList)) {
            if (cacheList.get(0).getId() == null) {
                return Collections.emptyList();
            }
            return cacheList;
        }
        LambdaQueryWrapper<PayProduct> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PayProduct::getType, type);
        queryWrapper.eq(PayProduct::getValidStatus, StatusEnum.VALID_STATUS.getCode());
        queryWrapper.orderByDesc(PayProduct::getPrice);
        List<PayProductDTO> payProductDTOS = ConvertBeanUtils.convertList(payProductMapper.selectList(queryWrapper), PayProductDTO.class);
        if (CollectionUtils.isEmpty(payProductDTOS)) {
            redisTemplate.opsForList().leftPush(cacheKey, new PayProductDTO());
            redisTemplate.expire(cacheKey, RedisKeyTime.EXPIRE_TIME_ONE_MINUTE, TimeUnit.SECONDS);
            return Collections.emptyList();
        }
        // List类型的putAll放入集合有bug，需要转换为数组
        redisTemplate.opsForList().leftPushAll(cacheKey, payProductDTOS.toArray());
        redisTemplate.expire(cacheKey, RedisKeyTime.EXPIRE_TIME_HALF_HOUR, TimeUnit.SECONDS);
        return payProductDTOS;
    }

    @Override
    public PayProductDTO getByProductId(Integer productId) {
        String cacheKey = payProviderCacheKeyBuilder.buildPayProductItemCache(productId);
        PayProductDTO payProductDTO = (PayProductDTO) redisTemplate.opsForValue().get(cacheKey);
        if (payProductDTO != null) {
            if (payProductDTO.getId() == null) {
                return null;
            }
            return payProductDTO;
        }
        LambdaQueryWrapper<PayProduct> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PayProduct::getId, productId);
        queryWrapper.eq(PayProduct::getValidStatus, StatusEnum.VALID_STATUS.getCode());
        queryWrapper.last("limit 1");
        payProductDTO = ConvertBeanUtils.convert(payProductMapper.selectOne(queryWrapper), PayProductDTO.class);
        if (payProductDTO == null) {
            redisTemplate.opsForValue().set(cacheKey, new PayProductDTO(), 1L, TimeUnit.MINUTES);
            return null;
        }
        redisTemplate.opsForValue().set(cacheKey, payProductDTO, 30L, TimeUnit.MINUTES);
        return payProductDTO;
    }

    @Override
    public PayProductRespVO payProduct(PayProductReqDTO payProductReqDTO) {
        // 参数校验
        if (payProductReqDTO == null || payProductReqDTO.getProductId() == null || payProductReqDTO.getPaySource() == null){
            throw new ParamException("参数不能为空");
        }
        if(PaySourceEnum.find(payProductReqDTO.getPaySource()) == null){
            throw new ParamException("支付渠道错误");
        }
        // 查询payProductDTO
        PayProductDTO payProductDTO = getByProductId(payProductReqDTO.getProductId());
        if (payProductDTO == null){
            throw new ParamException("产品不存在");
        }

        String userIdStr = RequestContext.get(HTTPKeyConstants.HTTP_HEADER_USER_ID).toString();
        Long userId = Long.valueOf(userIdStr);
        // 生成一条订单（待支付状态）
        PayOrder payOrder = new PayOrder();
        payOrder.setProductId(payProductReqDTO.getProductId())
                .setUserId(userId)
                .setPayTime(new Date())
                .setSource(payProductReqDTO.getPaySource())
                .setPayChannel(payProductReqDTO.getPayChannel());
        String orderId = payOrderService.insertOne(payOrder);
        // 模拟点击 去支付 按钮，更新订单状态为 支付中
        payOrderService.updateOrderStatus(orderId, OrderStatusEnum.PAYING.getCode());
        PayProductRespVO payProductRespVO = new PayProductRespVO();
        payProductRespVO.setOrderId(orderId);
        return payProductRespVO;
    }
}
