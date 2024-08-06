package top.linrty.live.im.handler.impl;

import top.linrty.live.common.domain.po.im.IMMsg;
import top.linrty.live.common.domain.po.im.IMMsgBody;
import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import top.linrty.live.common.constants.im.IMConstants;
import top.linrty.live.common.enums.im.IMMsgCodeEnum;
import top.linrty.live.im.handler.IMBaseHandler;
import top.linrty.live.im.utils.IMContext;
import top.linrty.live.im.utils.IMProviderCacheKeyBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @Description: 心跳消息处理器
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/31 23:06
 * @Version: 1.0
 **/
@Component
@Slf4j
public class HeartMsgHandler implements IMBaseHandler {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private IMProviderCacheKeyBuilder imProviderCacheKeyBuilder;

    @Override
    public void handler(ChannelHandlerContext ctx, IMMsg imMsg) {
        log.info("[heartbear]:" + imMsg);
        // 心跳包的基本校验
        Long userId = IMContext.getUserId(ctx);
        Integer appId = IMContext.getAppId(ctx);
        if (userId == null || appId == null) {
            log.error("attr error, imMsg is {}", imMsg);
            // 有可能是错误的消息包导致，直接放弃连接
            ctx.close();
            throw new IllegalArgumentException("attr error");
        }
        // 心跳包record记录
        String redisKey = imProviderCacheKeyBuilder.buildIMLoginTokenKey(userId, appId);
        this.recordOnlineTime(userId, redisKey);
        this.removeExpireRecord(redisKey);
        redisTemplate.expire(redisKey, 5L, TimeUnit.MINUTES);
        // 刷新缓存时间
        redisTemplate.expire(imProviderCacheKeyBuilder.buildIMLoginTokenKey(userId, appId)
                ,2 * IMConstants.DEFAULT_HEART_BEAT_GAP
                , TimeUnit.SECONDS );
        //回写给客户端
        IMMsgBody respBody = new IMMsgBody();
        respBody.setUserId(userId);
        respBody.setAppId(appId);
        respBody.setData("true");
        log.info("[HeartBeatImMsgHandler] heartbeat msg, userId is {}, appId is {}", userId, appId);
        ctx.writeAndFlush(IMMsg.build(IMMsgCodeEnum.IM_HEARTBEAT_MSG.getCode(), JSON.toJSONString(respBody)));
    }

    /**
     * 清理掉过期不在线的用户留下的心跳记录（两次心跳时间更友好）
     * 为什么不直接设置TTL让他自动过期？
     * 因为我们build redisKey的时候，是对userId%10000进行构建的，一个用户心跳记录只是zset中的一个键值对，而不是整个zset对象
     */
    private void removeExpireRecord(String redisKey) {
        redisTemplate.opsForZSet().removeRangeByScore(redisKey, 0, System.currentTimeMillis() - 2 * IMConstants.DEFAULT_HEART_BEAT_GAP * 1000);
    }

    /**
     * 记录用户最近一次心跳时间到Redis上
     */
    private void recordOnlineTime(Long userId, String redisKey) {
        redisTemplate.opsForZSet().add(redisKey, userId, System.currentTimeMillis());
    }
}
