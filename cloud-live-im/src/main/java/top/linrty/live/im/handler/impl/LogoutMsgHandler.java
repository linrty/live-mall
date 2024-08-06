package top.linrty.live.im.handler.impl;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import top.linrty.live.common.constants.im.IMMsgTopicNames;
import top.linrty.live.common.domain.dto.im.IMOfflineDTO;
import top.linrty.live.im.cache.ChannelCache;
import top.linrty.live.common.domain.po.im.IMMsg;
import top.linrty.live.common.domain.po.im.IMMsgBody;
import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import top.linrty.live.common.enums.im.IMMsgCodeEnum;
import top.linrty.live.im.handler.IMBaseHandler;
import top.linrty.live.im.utils.IMContext;
import top.linrty.live.im.utils.IMProviderCacheKeyBuilder;

import java.util.concurrent.CompletableFuture;

/**
 * @Description: 退出登录消息处理器
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/31 23:04
 * @Version: 1.0
 **/
@Component
@Slf4j
public class LogoutMsgHandler implements IMBaseHandler {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private IMProviderCacheKeyBuilder imProviderCacheKeyBuilder;

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void handler(ChannelHandlerContext ctx, IMMsg imMsg) {
        Long userId = IMContext.getUserId(ctx);
        Integer appId = IMContext.getAppId(ctx);
        if(userId == null || appId == null){
            log.error("attr error, imMsg is {}", imMsg);
            //有可能是错误的消息包导致，直接放弃连接
            ctx.close();
            throw new IllegalArgumentException("attr error");
        }
        //将IM消息回写给客户端
        IMMsgBody respBody = new IMMsgBody();
        respBody.setUserId(userId);
        respBody.setAppId(appId);
        respBody.setData("true");
        ctx.writeAndFlush(IMMsg.build(IMMsgCodeEnum.IM_LOGOUT_MSG.getCode(), JSON.toJSONString(respBody)));
        log.info("[LogoutMsgHandler] IM退出成功, userId is {}, appId is {}", userId, appId);
        // 删除用户缓存
        redisTemplate.delete(imProviderCacheKeyBuilder.buildIMBindIpKey(userId, appId));
        //理想情况下：客户端短线的时候发送短线消息包
        ChannelCache.remove(userId);
        IMContext.removeUserId(ctx);
        IMContext.removeAppId(ctx);
        ctx.close();
        // 删除心跳包存活缓存
        redisTemplate.delete(imProviderCacheKeyBuilder.buildIMLoginTokenKey(userId, appId));
    }

    public void handlerLogout(Long userId, Integer appId){
        // 理想情况下：客户端短线的时候发送短线消息包
        ChannelCache.remove(userId);
        // 删除Router取出的存在Redis的IM服务器的IP和端口地址
        redisTemplate.delete(imProviderCacheKeyBuilder.buildIMBindIpKey(userId, appId));
        // 删除心跳包存活缓存
        redisTemplate.delete(imProviderCacheKeyBuilder.buildIMLoginTokenKey(userId, appId));
    }

    public void logoutHandler(ChannelHandlerContext ctx, Long userId, Integer appId) {
        IMMsgBody respBody = new IMMsgBody();
        respBody.setUserId(userId);
        respBody.setAppId(appId);
        respBody.setData("true");
        ctx.writeAndFlush(IMMsg.build(IMMsgCodeEnum.IM_LOGOUT_MSG.getCode(), JSON.toJSONString(respBody)));
        log.info("[LogoutMsgHandler] logout success, userId is {}, appId is {}", userId, appId);
        handlerLogout(userId, appId);
        sendLogoutMQ(ctx, userId, appId);
        IMContext.removeUserId(ctx);
        IMContext.removeAppId(ctx);
        IMContext.removeRoomId(ctx);
        ctx.close();
    }

    /**
     * ws协议登出时，发送消息取消userId与roomId的关联
     */
    private void sendLogoutMQ(ChannelHandlerContext ctx, Long userId, Integer appId) {
        IMOfflineDTO imOfflineDTO = new IMOfflineDTO();
        imOfflineDTO.setUserId(userId)
                .setAppId(appId)
                .setRoomId(IMContext.getRoomId(ctx))
                .setLogoutTime(System.currentTimeMillis());
        CompletableFuture<SendResult<String, String>> sendResult = kafkaTemplate.send(IMMsgTopicNames.IM_OFFLINE_TOPIC, JSON.toJSONString(imOfflineDTO));
        sendResult.whenComplete((v, e) -> {
            if (e == null) {
                log.info("[sendLogoutMQ] send result is {}", sendResult);
            }
        }).exceptionally(e -> {
                    log.error("[sendLogoutMQ] send loginMQ error, error is ", e);
                    return null;
                }
        );
    }

}
