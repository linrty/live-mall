package top.linrty.live.im.handler.impl;

import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import top.linrty.live.common.domain.po.im.IMMsg;
import top.linrty.live.common.domain.po.im.IMMsgBody;
import top.linrty.live.im.handler.IMBaseHandler;
import top.linrty.live.im.service.IAckMsgService;
import top.linrty.live.im.utils.IMContext;
import top.linrty.live.im.utils.IMProviderCacheKeyBuilder;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/4 17:37
 * @Version: 1.0
 **/
@Component
@Slf4j
public class AckMsgHandler implements IMBaseHandler {


    @Resource
    private IAckMsgService ackMsgService;
    @Override
    public void handler(ChannelHandlerContext ctx, IMMsg imMsg) {
        Long userId = IMContext.getUserId(ctx);
        Integer appId = IMContext.getAppId(ctx);
        if (userId == null || appId == null){
            log.error("context is null , imMsgBody is {}", new String(imMsg.getBody()));
            // 有可能是错误的消息包导致的，直接放弃连接
            ctx.close();
            throw new IllegalArgumentException("context is null");
        }
        // 收到ack消息，删除未确认的消息
        ackMsgService.doMsgAck(JSON.parseObject(new String(imMsg.getBody()), IMMsgBody.class));
    }
}
