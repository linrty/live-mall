package top.linrty.live.pay.service.impl;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import top.linrty.live.api.clients.PayClient;
import top.linrty.live.common.domain.dto.pay.AccountTradeReqDTO;
import top.linrty.live.common.domain.dto.pay.AccountTradeRespDTO;
import top.linrty.live.common.domain.dto.pay.PayProductDTO;
import top.linrty.live.pay.service.ICurrencyAccountService;
import top.linrty.live.pay.service.IPayProductService;

import java.util.List;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/6 23:31
 * @Version: 1.0
 **/
@DubboService
public class PayClientImpl implements PayClient {

    @Resource
    private ICurrencyAccountService currencyAccountService;

    @Resource
    private IPayProductService payProductService;

    @Override
    public Integer getBalance(Long userId) {
        return currencyAccountService.getBalance(userId);
    }

    @Override
    public AccountTradeRespDTO consumeForSendGift(AccountTradeReqDTO accountTradeReqDTO) {
        return currencyAccountService.consumeForSendGift(accountTradeReqDTO);
    }

    @Override
    public List<PayProductDTO> products(Integer type) {
        return payProductService.products(type);
    }

    @Override
    public PayProductDTO getByProductId(Integer productId) {
        return payProductService.getByProductId(productId);
    }

    @Override
    public void incrCurrencyAccount(Long userId, int num) {
        currencyAccountService.incr(userId, num);
    }

    @Override
    public void decrCurrencyAccount(Long userId, int num) {
        currencyAccountService.decr(userId, num);
    }
}
