package top.linrty.live.im.handler;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.linrty.live.common.domain.po.im.IMMsg;
import top.linrty.live.im.handler.impl.LogoutMsgHandler;
import top.linrty.live.im.utils.IMContext;

/**
 * @Description: WebSocket连接处理器
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/6 15:39
 * @Version: 1.0
 **/
@Component
@Slf4j
@ChannelHandler.Sharable
public class WsImServerCoreHandler extends SimpleChannelInboundHandler {


    @Resource
    private IMHandlerFactory imHandlerFactory;

    @Resource
    private LogoutMsgHandler logoutMsgHandler;


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        wsMsgHandler(channelHandlerContext, (WebSocketFrame) msg);
    }
    /**
     * 客户端正常或意外掉线，都会触发这里
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Long userId = IMContext.getUserId(ctx);
        int appId = IMContext.getAppId(ctx);
        logoutMsgHandler.logoutHandler(ctx, userId, appId);
    }

    private void wsMsgHandler(ChannelHandlerContext ctx, WebSocketFrame msg) {
        // 如过不是文本消息，统一后台不会处理
        if (!(msg instanceof TextWebSocketFrame)) {
            log.error("[WsImServerCoreHandler] wsMsgHandler, {} msg types not supported", msg.getClass().getName());
            return;
        }
        try {
            // 返回应答消息
            String content = (((TextWebSocketFrame) msg).text());
            JSONObject jsonObject = JSON.parseObject(content, JSONObject.class);
            IMMsg imMsg = new IMMsg();
            imMsg.setMagic(jsonObject.getShort("magic"))
                    .setCode(jsonObject.getInteger("code"))
                    .setLen(jsonObject.getInteger("len"))
                    .setBody(jsonObject.getString("body").getBytes());
            imHandlerFactory.doMsgHandler(ctx, imMsg);
        } catch (Exception e) {
            log.error("[WsImServerCoreHandler] wsMsgHandler error is ", e);
        }
    }
}
