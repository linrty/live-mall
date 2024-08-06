package top.linrty.live.pay.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.linrty.live.common.enums.StatusEnum;
import top.linrty.live.pay.domain.po.CurrencyTrade;
import top.linrty.live.pay.mapper.CurrencyTradeMapper;
import top.linrty.live.pay.service.ICurrencyTradeService;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 1:33
 * @Version: 1.0
 **/
@Service
@Slf4j
public class CurrencyTradeServiceImpl implements ICurrencyTradeService {

    @Resource
    private CurrencyTradeMapper currencyTradeMapper;

    @Override
    public boolean insertOne(Long userId, int num, int type) {
        try {
            CurrencyTrade tradePO = new CurrencyTrade();
            tradePO.setUserId(userId);
            tradePO.setNum(num);
            tradePO.setType(type);
            tradePO.setStatus(StatusEnum.VALID_STATUS.getCode());
            currencyTradeMapper.insert(tradePO);
            return true;
        } catch (Exception e) {
            log.error("[QiyuCurrencyTradeServiceImpl] insert error, error is:", e);
        }
        return false;
    }
}
