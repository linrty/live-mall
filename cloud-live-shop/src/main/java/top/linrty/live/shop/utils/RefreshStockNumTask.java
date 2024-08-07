package top.linrty.live.shop.utils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import top.linrty.live.shop.service.IAnchorShopInfoService;
import top.linrty.live.shop.service.ISkuInfoService;
import top.linrty.live.shop.service.ISkuStockInfoService;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 21:56
 * @Version: 1.0
 **/
@Configuration
@EnableScheduling
@Slf4j
public class RefreshStockNumTask {

    @Resource
    private ShopProviderCacheKeyBuilder shopProviderCacheKeyBuilder;

    @Resource
    private IAnchorShopInfoService anchorShopInfoService;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private ISkuStockInfoService skuStockInfoService;

    @Scheduled(cron = "*/15 * * * * ? ")
    public void refreshStockNum() {
        String lockKey = shopProviderCacheKeyBuilder.buildStockSyncLock();
        RLock lock = redissonClient.getLock(lockKey);
        try{
            Boolean isLock = lock.tryLock(1, 15L, TimeUnit.SECONDS);
            if (Boolean.TRUE.equals(isLock)) {
                List<Long> anchorIdList = anchorShopInfoService.queryAllValidAnchorId();
                for (Long anchorId : anchorIdList) {
                    skuStockInfoService.syncStockNumToMySQL(anchorId);
                }
            }else{
                log.error("获取锁失败");
            }
        }catch (Exception e){
            log.error("获取锁失败", e);
        } finally {
            lock.unlock();
        }
    }
}
