package top.linrty.live.im.handler;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import top.linrty.live.common.utils.JwtUtils;
import top.linrty.live.im.handler.impl.LoginMsgHandler;

/**
 * @Description: WebSocket协议的握手连接处理器
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/6 16:07
 * @Version: 1.0
 **/
@Component
@ChannelHandler.Sharable
@RefreshScope
@Slf4j
public class WsSharkHandler extends ChannelInboundHandlerAdapter {

    @Value("${live.im.ws.port}")
    private int port;

    @Resource
    private LoginMsgHandler loginMsgHandler;
    @Resource
    private Environment environment;

    private WebSocketServerHandshaker webSocketServerHandshaker;

    @Resource
    private JwtUtils jwtUtils;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 握手连接接入ws
        if (msg instanceof FullHttpRequest) {
            handlerHttpRequest(ctx, (FullHttpRequest) msg);
            return;
        }
        // 正常关闭链路
        if (msg instanceof CloseWebSocketFrame) {
            webSocketServerHandshaker.close(ctx.channel(), (CloseWebSocketFrame) ((WebSocketFrame) msg).retain());
            return;
        }
        ctx.fireChannelRead(msg);
    }

    private void handlerHttpRequest(ChannelHandlerContext ctx, FullHttpRequest msg) {
        String serverIp = environment.getProperty("DUBBO_IP_TO_REGISTRY");
        String webSocketUrl = "ws://" + serverIp + ":" + port;
        // 构造握手响应返回
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(webSocketUrl, null, false);
        // 进行参数校验
        String uri = msg.uri();
        String[] paramArr = uri.split("/");
        String token = paramArr[1];
        Long userId = Long.valueOf(paramArr[2]);
        Long queryUserId = jwtUtils.getUserIdByToken(token) == null ? null : Long.parseLong(jwtUtils.getUserIdByToken(token));
        //token的最后与%拼接的就是appId
        Integer appId = Integer.valueOf(token.substring(token.indexOf("%") + 1));
        if (queryUserId == null || !queryUserId.equals(userId)) {
            log.error("[WsSharkHandler] token 校验不通过！");
            ctx.close();
            return;
        }
        // 参数校验通过，建立ws握手连接
        webSocketServerHandshaker = wsFactory.newHandshaker(msg);
        if (webSocketServerHandshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            return;
        }
        ChannelFuture channelFuture = webSocketServerHandshaker.handshake(ctx.channel(), msg);
        // 首次握手建立ws连接后，返回一定的内容给到客户端
        if (channelFuture.isSuccess()) {
            Integer code = Integer.valueOf(paramArr[3]);
            Integer roomId = null;
            if (code == ParamCodeEnum.LIVING_ROOM_LOGIN.getCode()) {
                roomId = Integer.valueOf(paramArr[4]);
            }
            //这里调用login消息包的处理器，直接做login成功的处理
            loginMsgHandler.loginSuccessHandler(ctx, userId, appId,null);
            log.info("[WsSharkHandler] channel is connect");
        }
    }

    enum ParamCodeEnum {
        LIVING_ROOM_LOGIN(1001, "直播间登录");
        int code;
        String desc;
        ParamCodeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        public int getCode() {
            return code;
        }
        public String getDesc() {
            return desc;
        }
    }
}
