package top.linrty.live.pay.service;

import top.linrty.live.pay.domain.po.PayOrder;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 1:58
 * @Version: 1.0
 **/
public interface IPayOrderService {
    /**
     * 根据orderId查询订单信息
     */
    PayOrder queryByOrderId(String orderId);

    /**
     *插入订单 ，返回主键id
     */
    String insertOne(PayOrder payOrderPO);

    /**
     * 根据主键id更新订单状态
     */
    boolean updateOrderStatus(Long id, Integer status);

    /**
     * 更新订单状态
     */
    boolean updateOrderStatus(String orderId, Integer status);
}
