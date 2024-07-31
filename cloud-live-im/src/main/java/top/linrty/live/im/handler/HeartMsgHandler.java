package top.linrty.live.im.handler;

import io.netty.channel.ChannelHandlerContext;
import top.linrty.live.im.domain.po.ImMsg;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/31 23:06
 * @Version: 1.0
 **/
public class HeartMsgHandler implements IMBaseHandler{
    @Override
    public void handler(ChannelHandlerContext ctx, ImMsg imMsg) {
        System.out.println("[heartMsg]:" + imMsg);
        ctx.writeAndFlush(imMsg);
    }
}
