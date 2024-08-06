package top.linrty.live.im.handler;

import top.linrty.live.common.domain.po.im.IMMsg;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/31 23:03
 * @Version: 1.0
 **/
public interface IMBaseHandler {
    void handler(ChannelHandlerContext ctx, IMMsg imMsg);
}
