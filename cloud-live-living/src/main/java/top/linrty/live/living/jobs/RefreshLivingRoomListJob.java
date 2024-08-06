package top.linrty.live.living.jobs;

import cn.hutool.core.util.BooleanUtil;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import top.linrty.live.common.enums.living.LivingRoomTypeEnum;
import top.linrty.live.common.domain.vo.living.LivingRoomRespVO;
import top.linrty.live.living.service.ILivingRoomService;
import top.linrty.live.living.utils.LivingProviderCacheKeyBuilder;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/5 23:20
 * @Version: 1.0
 **/
@Configuration
@Slf4j
public class RefreshLivingRoomListJob implements InitializingBean {

    @Resource
    private ILivingRoomService livingRoomService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private LivingProviderCacheKeyBuilder livingProviderCacheKeyBuilder;


    @Resource
    private RedissonClient redissonClient;


    private static final ScheduledThreadPoolExecutor SCHEDULED_THREAD_POOL_EXECUTOR = new ScheduledThreadPoolExecutor(1);




    @Override
    public void afterPropertiesSet() throws Exception {
        //每十秒检查一次需不需要刷新直播间列表
        SCHEDULED_THREAD_POOL_EXECUTOR.scheduleWithFixedDelay(new RefreshLivingRoomListThread(), 3000, 10000, TimeUnit.MILLISECONDS);
    }

    class RefreshLivingRoomListThread implements Runnable {
        @Override
        public void run() {
            String cacheKey = livingProviderCacheKeyBuilder.buildRefreshLivingRoomListLock();
            //这把锁等他自动过期
            RLock lock = redissonClient.getLock(cacheKey);
            try{
                Boolean isLock = lock.tryLock(10L, TimeUnit.SECONDS);
                if (BooleanUtil.isFalse(isLock)) {
                    log.info("[RefreshLivingRoomListJob] 获取锁失败");
                    return;
                }
                log.info("[RefreshLivingRoomListJob] starting  更新数据库中记录的直播间到Redis中去");
                refreshDBTiRedis(LivingRoomTypeEnum.DEFAULT_LIVING_ROOM.getCode());
                refreshDBTiRedis(LivingRoomTypeEnum.PK_LIVING_ROOM.getCode());
                log.info("[RefreshLivingRoomListJob] end  更新数据库中记录的直播间到Redis中去");
            }catch (Exception e){
                log.error("[RefreshLivingRoomListJob] 获取锁失败");
            } finally {
                lock.unlock();
            }
        }
    }

    private void refreshDBTiRedis(Integer type) {
        //获取直播间列表刷新标志
        Object refreshFlag = redisTemplate.opsForValue().get(livingProviderCacheKeyBuilder.buildLivingRoomListRefreshFlag(type));
        if( refreshFlag == null || !(boolean) refreshFlag) return;
        String cacheKey = livingProviderCacheKeyBuilder.buildLivingRoomList(type);
        List<LivingRoomRespVO> resultList = livingRoomService.listAllLivingRoomFromDB(type);
        if (CollectionUtils.isEmpty(resultList)) {
            redisTemplate.unlink(cacheKey);
            return;
        }
        String tempListName = cacheKey + "_temp";
        //需要一行一行push进去，pushAll方法有bug，会添加到一条记录里去
        for (LivingRoomRespVO livingRoomRespVO : resultList) {
            redisTemplate.opsForList().rightPush(tempListName, livingRoomRespVO);
        }
        //直接修改重命名这个list，不要直接对原来的list进行修改，减少阻塞的影响
        redisTemplate.rename(tempListName, cacheKey);
        redisTemplate.unlink(tempListName);
        redisTemplate.opsForValue().set(livingProviderCacheKeyBuilder.buildLivingRoomListRefreshFlag(type), false);
    }
}
