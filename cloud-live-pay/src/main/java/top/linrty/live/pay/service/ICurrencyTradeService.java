package top.linrty.live.pay.service;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 1:32
 * @Version: 1.0
 **/
public interface ICurrencyTradeService {
    boolean insertOne(Long userId, int num, int type);
}
