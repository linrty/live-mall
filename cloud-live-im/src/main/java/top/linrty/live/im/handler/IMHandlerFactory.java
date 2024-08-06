package top.linrty.live.im.handler;

import top.linrty.live.common.domain.po.im.IMMsg;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import top.linrty.live.common.enums.im.IMMsgCodeEnum;
import top.linrty.live.im.handler.impl.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/31 21:46
 * @Version: 1.0
 **/
@Component
@Slf4j
public class IMHandlerFactory implements InitializingBean {
    private static Map<Integer, IMBaseHandler> handlerMap = new HashMap<>();

    @Resource
    private ApplicationContext applicationContext;
    public void doMsgHandler(ChannelHandlerContext ctx, IMMsg imMsg) {
        IMBaseHandler imBaseHandler = handlerMap.get(imMsg.getCode());
        if(imBaseHandler == null) {
            throw new IllegalArgumentException("msg code is error, code is :" + imMsg.getCode());
        }
        imBaseHandler.handler(ctx, imMsg);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        //登录消息包：登录token验证，channel 和 userId 关联
        //登出消息包：正常断开im连接时发送的
        //业务消息包：最常用的消息类型，例如我们的im收发数据
        //心跳消息包：定时给im发送心跳包
        log.info("IMHandlerFactory 初始化成功");
        handlerMap.put(IMMsgCodeEnum.IM_LOGIN_MSG.getCode(), applicationContext.getBean(LoginMsgHandler.class));
        handlerMap.put(IMMsgCodeEnum.IM_LOGOUT_MSG.getCode(), applicationContext.getBean(LogoutMsgHandler.class));
        handlerMap.put(IMMsgCodeEnum.IM_BIZ_MSG.getCode(), applicationContext.getBean(BizMsgHandler.class));
        handlerMap.put(IMMsgCodeEnum.IM_HEARTBEAT_MSG.getCode(), applicationContext.getBean(HeartMsgHandler.class));
        handlerMap.put(IMMsgCodeEnum.IM_ACK_MSG.getCode(), applicationContext.getBean(AckMsgHandler.class));
    }
}
