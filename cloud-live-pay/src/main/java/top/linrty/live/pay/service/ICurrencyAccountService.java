package top.linrty.live.pay.service;

import top.linrty.live.common.domain.dto.pay.AccountTradeReqDTO;
import top.linrty.live.common.domain.dto.pay.AccountTradeRespDTO;
import top.linrty.live.common.domain.vo.pay.CurrencyAccountVO;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/6 23:09
 * @Version: 1.0
 **/
public interface ICurrencyAccountService {
    /**
     * 新增账户
     */
    boolean insertOne(Long userId);

    /**
     * 增加虚拟货币
     */
    void incr(Long userId, int num);

    /**
     * 扣减虚拟币
     */
    void decr(Long userId, int num);

    /**
     * 查询账户
     */
    CurrencyAccountVO getByUserId(Long userId);

    /**
     * 查询账户余额
     */
    Integer getBalance(Long userId);

    /**
     * 专门给送礼用的扣减库存逻辑，进行了高并发优化
     */
    AccountTradeRespDTO consumeForSendGift(AccountTradeReqDTO accountTradeReqDTO);

}
