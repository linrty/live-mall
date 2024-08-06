package top.linrty.live.pay.utils;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import top.linrty.live.common.config.redis.RedisKeyBuilder;
import top.linrty.live.common.config.redis.RedisKeyLoadMatch;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/6 22:38
 * @Version: 1.0
 **/
@Configuration
@Conditional(RedisKeyLoadMatch.class)
public class PayProviderCacheKeyBuilder extends RedisKeyBuilder {
    private static final String BALANCE_CACHE = "balance_cache";

    private static final String BALANCE_CACHE_LOCK = "balance_cache_lock";

    private static final String PAY_PRODUCT_CACHE = "pay_product_cache";

    private static final String PAY_PRODUCT_ITEM_CACHE = "pay_product_item_cache";

    private static final String PAY_ORDER_ID_INCR_KEY_PREFIX = "payOrderId";

    public String buildPayProductItemCache(Integer productId) {
        return getPrefix() + PAY_PRODUCT_ITEM_CACHE + getSplitItem() + productId;
    }

    /**
     * 按照产品的类型来进行检索
     *
     * @param type
     * @return
     */
    public String buildPayProductCache(Integer type) {
        return getPrefix() + PAY_PRODUCT_CACHE + getSplitItem() + type;
    }

    /**
     * 构建用户余额cache key
     *
     * @param userId
     * @return
     */
    public String buildUserBalance(Long userId) {
        return getPrefix() + BALANCE_CACHE + getSplitItem() + userId;
    }

    public String buildUserBalanceLock(Long userId) {
        return getPrefix() + BALANCE_CACHE_LOCK + getSplitItem() + userId;
    }

    public String buildPayOrderIdIncrKey() {
        return getPrefix() + PAY_ORDER_ID_INCR_KEY_PREFIX;
    }
}
