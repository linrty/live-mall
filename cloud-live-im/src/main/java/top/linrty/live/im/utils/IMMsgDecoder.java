package top.linrty.live.im.utils;

import top.linrty.live.common.domain.po.im.IMMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import top.linrty.live.common.constants.im.IMConstants;

import java.util.List;

/**
 * @Description: 处理消息的解码过程
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/31 17:00
 * @Version: 1.0
 **/
public class IMMsgDecoder extends ByteToMessageDecoder {
    //ImMsg的最低基本字节数
    private final int BASE_LEN = 2 + 4 + 4;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> out) throws Exception {
        //进行byteBuf内容的基本校验：长度校验 和 magic值校验
        if(byteBuf.readableBytes() >= BASE_LEN) {
            if(byteBuf.readShort() != IMConstants.DEFAULT_MAGIC) {
                channelHandlerContext.close();
                return;
            }
            int code = byteBuf.readInt();
            int len = byteBuf.readInt();
            //byte数组的字节数小于len，说明消息不完整
            if(byteBuf.readableBytes() < len) {
                channelHandlerContext.close();
                return;
            }
            byte[] body = new byte[len];
            byteBuf.readBytes(body);
            //将byteBuf转换为ImMsg对象
            IMMsg imMsg = new IMMsg();
            imMsg.setCode(code);
            imMsg.setLen(len);
            imMsg.setBody(body);
            imMsg.setMagic(IMConstants.DEFAULT_MAGIC);
            out.add(imMsg);
        }
    }
}
