package top.linrty.live.shop.utils;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import top.linrty.live.common.config.redis.RedisKeyBuilder;
import top.linrty.live.common.config.redis.RedisKeyLoadMatch;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 17:13
 * @Version: 1.0
 **/
@Configuration
@Conditional(RedisKeyLoadMatch.class)
public class ShopProviderCacheKeyBuilder extends RedisKeyBuilder {
    private static final String SKU_DETAIL_INFO_MAP = "sku_detail_info_map";
    private static final String SHOP_CAR = "shop_car";
    private static final String SKU_STOCK = "sku_stock";
    private static final String STOCK_SYNC_LOCK = "stock_sync_lock";

    public String buildStockSyncLock() {
        return super.getPrefix() + STOCK_SYNC_LOCK;
    }

    public String buildSkuStock(Long skuId) {
        return super.getPrefix() + SKU_STOCK + super.getSplitItem() + skuId;
    }

    public String buildUserShopCar(Long userId, Integer roomId) {
        return super.getPrefix() + SKU_DETAIL_INFO_MAP + super.getSplitItem() + userId + super.getSplitItem() + roomId;
    }

    public String buildSkuDetailInfoMap(Long anchorId) {
        return super.getPrefix() + SKU_DETAIL_INFO_MAP + super.getSplitItem() + anchorId;
    }
}
