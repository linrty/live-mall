package top.linrty.live.im.handler;


import top.linrty.live.common.domain.po.im.IMMsg;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.linrty.live.im.cache.ChannelCache;
import top.linrty.live.im.handler.impl.LogoutMsgHandler;
import top.linrty.live.im.utils.IMContext;
import top.linrty.live.im.utils.IMContextAttr;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/31 21:44
 * @Version: 1.0
 **/
@Component
@ChannelHandler.Sharable
public class IMServerHandler extends SimpleChannelInboundHandler<IMMsg> {

    /**
     * 工厂模式管理不同的消息包处理策略
     */
    private static IMHandlerFactory imHandlerFactory;

    @Resource
    private LogoutMsgHandler logoutMsgHandler;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, IMMsg msg) throws Exception {
        ReferenceCountUtil.retain(msg);
        imHandlerFactory.doMsgHandler(channelHandlerContext, msg);
    }

    @Autowired
    public void setImHandlerFactory(IMHandlerFactory imHandlerFactory) {
        IMServerHandler.imHandlerFactory = imHandlerFactory;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Long userId = IMContext.getUserId(ctx);
        Integer appId = IMContext.getAppId(ctx);
        logoutMsgHandler.handlerLogout(userId, appId);
    }
}
