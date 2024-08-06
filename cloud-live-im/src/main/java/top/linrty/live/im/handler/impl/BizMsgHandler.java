package top.linrty.live.im.handler.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import top.linrty.live.common.constants.im.IMMsgTopicNames;
import top.linrty.live.common.domain.po.im.IMMsg;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;
import top.linrty.live.im.handler.IMBaseHandler;
import top.linrty.live.im.utils.IMContext;

import java.util.concurrent.CompletableFuture;

/**
 * @Description: 业务消息处理器
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/31 23:05
 * @Version: 1.0
 **/
@Component
@Slf4j
public class BizMsgHandler implements IMBaseHandler {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void handler(ChannelHandlerContext ctx, IMMsg imMsg) {
        // 前期的参数校验
        Long userId = IMContext.getUserId(ctx);
        Integer appId = IMContext.getAppId(ctx);
        if (userId == null || appId == null) {
            log.error("attr error, imMsg is {}", imMsg);
            // 有可能是错误的消息包导致，直接放弃连接
            ctx.close();
            throw new IllegalArgumentException("attr error");
        }
        byte[] body = imMsg.getBody();
        if (body == null || body.length == 0) {
            log.error("body error ,imMsg is {}", imMsg);
            return;
        }
        // 发送消息
        CompletableFuture<SendResult<String, String>> sendResult = kafkaTemplate.send(IMMsgTopicNames.IM_BIZ_MSG_TOPIC, new String(body));
        sendResult.whenComplete((v, e) -> {
            if (e == null) {
                log.info("[BizImMsgHandler]消息投递成功, sendResult is {}", v);
            }
        }).exceptionally(e -> {
            log.error("send error, error is :", e);
            throw new RuntimeException(e);
        });
    }
}
