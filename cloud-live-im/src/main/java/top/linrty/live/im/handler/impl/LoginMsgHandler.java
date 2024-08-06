package top.linrty.live.im.handler.impl;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import top.linrty.live.common.constants.im.IMConstants;
import top.linrty.live.common.constants.im.IMMsgTopicNames;
import top.linrty.live.common.domain.dto.im.IMOnlineDTO;
import top.linrty.live.im.cache.ChannelCache;
import top.linrty.live.common.domain.po.im.IMMsg;
import top.linrty.live.common.domain.po.im.IMMsgBody;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.linrty.live.common.enums.BizEnum;
import top.linrty.live.common.enums.im.IMMsgCodeEnum;
import top.linrty.live.common.utils.JwtUtils;
import top.linrty.live.im.handler.IMBaseHandler;
import top.linrty.live.im.utils.IMContext;
import top.linrty.live.im.utils.IMProviderCacheKeyBuilder;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 登录消息处理器
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/31 23:04
 * @Version: 1.0
 **/
@Component
@Slf4j
public class LoginMsgHandler implements IMBaseHandler {


    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private IMProviderCacheKeyBuilder imProviderCacheKeyBuilder;

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;



    /**
     * 如果需要建立连接，需要进行一些参数的校验
     * 当参数校验通过后，保证userId是相同的
     * @param ctx
     * @param imMsg
     */
    @Override
    public void handler(ChannelHandlerContext ctx, IMMsg imMsg) {
        // 防止重复请求：login允许连接才放如userId，若已经允许连接就不再接收login请求包
        if (IMContext.getUserId(ctx) != null) {
            return;
        }
        byte[] body = imMsg.getBody();
        // 验证body
        if (body == null || body.length == 0) {
            ctx.close();
            log.error("消息体错误,消息内容： {}", imMsg);
            throw new IllegalArgumentException("消息体错误");
        }
        IMMsgBody imMsgBody = JSON.parseObject(new String(body), IMMsgBody.class);
        String token = imMsgBody.getToken();
        Long userIdFromMsg = imMsgBody.getUserId();
        int appId = imMsgBody.getAppId();
        if (StrUtil.isEmpty(token) || userIdFromMsg < 10000 || appId < 1000) {
            ctx.close();
            log.error("参数错误, 消息内容： {}", imMsg);
            throw new IllegalArgumentException("参数错误");
        }
        String userIdStr = jwtUtils.getUserIdByToken(token);
        Long userId = StrUtil.isEmpty(userIdStr)? 0 : Long.parseLong(userIdStr);
        if (userIdFromMsg.equals(userId)){
            loginSuccessHandler(ctx, userId, appId, null);
            return;
        }
        // 不允许建立连接
        ctx.close();
        log.error("token错误, 消息内容： {}", imMsg);
        throw new IllegalArgumentException("token异常");
    }

    /**
     * 如果用户成功登录，就处理相关记录
     */
    public void loginSuccessHandler(ChannelHandlerContext ctx, Long userId, Integer appId, Integer roomId) {
        // 按照userId保存好相关的channel信息
        ChannelCache.put(userId, ctx);
        // 将userId保存到netty域信息中，用于正常/非正常logout的处理
        IMContext.setUserId(ctx, userId);
        IMContext.setAppId(ctx, appId);
        // 将im消息回写给客户端
        IMMsgBody respBody = new IMMsgBody()
                .setAppId(BizEnum.LIVE_BIZ.getCode())
                .setUserId(userId)
                .setData("true");
        IMMsg respMsg = IMMsg.build(IMMsgCodeEnum.IM_LOGIN_MSG.getCode(), JSON.toJSONString(respBody));
        // 将im服务器的ip+端口地址保存到Redis，以供Router服务取出进行转发
        redisTemplate.opsForValue().set(imProviderCacheKeyBuilder.buildIMBindIpKey(userId, appId)
                , ChannelCache.getServerIp() + "%" + userId
                ,2 * IMConstants.DEFAULT_HEART_BEAT_GAP
                , TimeUnit.SECONDS);
        log.info("[LoginMsgHandler] login success, userId is {}, appId is {}", userId, appId);
        ctx.writeAndFlush(respMsg);
        sendLoginMQ(userId, appId, roomId);
    }

    /**
     * ws协议用户初次登录的时候发送mq消息，将userId与roomId关联起来，便于直播间聊天消息的推送
     */
    private void sendLoginMQ(Long userId, Integer appId, Integer roomId) {
        IMOnlineDTO imOnlineDTO = new IMOnlineDTO()
                .setUserId(userId)
                .setAppId(appId)
                .setRoomId(roomId)
                .setLoginTime(System.currentTimeMillis());
        CompletableFuture<SendResult<String, String>> sendResult = kafkaTemplate.send(IMMsgTopicNames.IM_ONLINE_TOPIC, JSON.toJSONString(imOnlineDTO));
        sendResult.whenComplete((v, e) -> {
            if (e == null) {
                log.info("[sendLoginMQ] send result is {}", sendResult);
            }
        }).exceptionally(e -> {
                    log.error("[sendLoginMQ] send loginMQ error, error is ", e);
                    return null;
                }
        );
    }
}
