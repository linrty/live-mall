package top.linrty.live.shop.service;

import top.linrty.live.common.domain.dto.shop.PrepareOrderDTO;
import top.linrty.live.common.domain.dto.shop.SkuOrderInfoReqDTO;
import top.linrty.live.common.domain.vo.shop.SkuOrderInfoRespVO;
import top.linrty.live.common.domain.vo.shop.SkuPrepareOrderInfoVO;
import top.linrty.live.shop.domain.po.SkuOrderInfo;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 21:18
 * @Version: 1.0
 **/
public interface ISkuOrderInfoService {
    /**
     * 根据userId和roomId查询订单信息
     */
    SkuOrderInfoRespVO queryByUserIdAndRoomId(Long userId, Integer roomId);

    /**
     * 插入一条订单
     */
    SkuOrderInfo insertOne(SkuOrderInfoReqDTO skuOrderInfoReqDTO);

    /**
     * 更新订单状态
     */
    boolean updateOrderStatus(SkuOrderInfoReqDTO skuOrderInfoReqDTO);

    SkuOrderInfoRespVO queryByOrderId(Long orderId);

    SkuPrepareOrderInfoVO prepareOrder(PrepareOrderDTO reqDTO);

    /**
     * 用户进行订单支付
     */
    boolean payNow(PrepareOrderDTO prepareOrderDTO);
}
