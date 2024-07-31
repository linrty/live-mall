package top.linrty.live.im.handler;

import io.netty.channel.ChannelHandlerContext;
import top.linrty.live.common.enums.IMMsgCodeEnum;
import top.linrty.live.im.domain.po.ImMsg;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/31 21:46
 * @Version: 1.0
 **/
public class IMHandlerFactory {
    private static Map<Integer, IMBaseHandler> handlerMap = new HashMap<>();

    static {
        //登录消息包：登录token验证，channel 和 userId 关联
        //登出消息包：正常断开im连接时发送的
        //业务消息包：最常用的消息类型，例如我们的im收发数据
        //心跳消息包：定时给im发送心跳包
        handlerMap.put(IMMsgCodeEnum.IM_LOGIN_MSG.getCode(), new LoginMsgHandler());
        handlerMap.put(IMMsgCodeEnum.IM_LOGOUT_MSG.getCode(), new LogoutMsgHandler());
        handlerMap.put(IMMsgCodeEnum.IM_BIZ_MSG.getCode(), new BizMsgHandler());
        handlerMap.put(IMMsgCodeEnum.IM_HEARTBEAT_MSG.getCode(), new HeartMsgHandler());
    }

    public void doMsgHandler(ChannelHandlerContext ctx, ImMsg imMsg) {
        IMBaseHandler imBaseHandler = handlerMap.get(imMsg.getCode());
        if(imBaseHandler == null) {
            throw new IllegalArgumentException("msg code is error, code is :" + imMsg.getCode());
        }
        imBaseHandler.handler(ctx, imMsg);
    }
}
