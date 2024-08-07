package top.linrty.live.api.clients;

import top.linrty.live.common.domain.dto.pay.AccountTradeReqDTO;
import top.linrty.live.common.domain.dto.pay.AccountTradeRespDTO;
import top.linrty.live.common.domain.dto.pay.PayProductDTO;

import java.util.List;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/6 23:26
 * @Version: 1.0
 **/
public interface PayClient {
    /**
     * 查询账户余额
     */
    Integer getBalance(Long userId);

    /**
     * 专门给送礼用的扣减库存逻辑，进行了高并发优化
     */
    AccountTradeRespDTO consumeForSendGift(AccountTradeReqDTO accountTradeReqDTO);

    /**
     * 根据产品类型，返回批量的商品信息
     */
    List<PayProductDTO> products(Integer type);

    /**
     * 根据产品id查询
     */
    PayProductDTO getByProductId(Integer productId);

    void incrCurrencyAccount(Long userId, int num);

    void decrCurrencyAccount(Long userId, int num);

}
