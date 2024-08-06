package top.linrty.top.client.utils;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import top.linrty.top.client.po.IMMsg;

/**
 * @Description: 处理消息的编码过程
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/31 16:59
 * @Version: 1.0
 **/
public class IMMsgEncoder extends MessageToByteEncoder{
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object msg, ByteBuf out) throws Exception {
        IMMsg imMsg = (IMMsg) msg;
        //按照ImMsg属性的类型顺序
        out.writeShort(imMsg.getMagic());
        out.writeInt(imMsg.getCode());
        out.writeInt(imMsg.getLen());
        out.writeBytes(imMsg.getBody());
        channelHandlerContext.writeAndFlush(out);
    }
}
