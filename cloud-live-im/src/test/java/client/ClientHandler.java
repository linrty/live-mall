package client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import top.linrty.live.common.domain.po.im.IMMsg;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/31 23:09
 * @Version: 1.0
 **/
public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        IMMsg imMsg = (IMMsg) msg;
        System.out.println("【服务端响应数据】 result is " + imMsg);
    }
}
