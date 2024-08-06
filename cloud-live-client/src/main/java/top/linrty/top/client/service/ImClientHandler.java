package top.linrty.top.client.service;


import com.alibaba.fastjson2.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ReferenceCountUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import top.linrty.live.common.enums.BizEnum;
import top.linrty.live.common.enums.im.IMMsgCodeEnum;
import top.linrty.live.common.utils.JwtUtils;
import top.linrty.top.client.handler.ClientHandler;
import top.linrty.top.client.po.IMMsg;
import top.linrty.top.client.po.IMMsgBody;
import top.linrty.top.client.utils.IMMsgDecoder;
import top.linrty.top.client.utils.IMMsgEncoder;

import java.util.Map;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/1 21:18
 * @Version: 1.0
 **/
@Service
public class ImClientHandler implements InitializingBean {

    @Resource
    private JwtUtils jwtUtils;
    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NioEventLoopGroup clientGroup = new NioEventLoopGroup();
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(clientGroup);
                bootstrap.channel(NioSocketChannel.class);
                bootstrap.handler(new ChannelInitializer<>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        System.out.println("初始化连接建立");
                        channel.pipeline().addLast(new IMMsgEncoder());
                        channel.pipeline().addLast(new IMMsgDecoder());
                        channel.pipeline().addLast(new ClientHandler());
                    }
                });
                ChannelFuture channelFuture = null;
                try {
                    channelFuture = bootstrap.connect("localhost", 8082).sync();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Long userId = 11113L;
                Channel channel = channelFuture.channel();
                Map<String, Object> param = Map.of("userId", userId, "appId", BizEnum.LIVE_BIZ.getCode());
                String token = jwtUtils.createJWT(param);
                IMMsgBody imMsgBody = new IMMsgBody();
                imMsgBody.setUserId(userId);
                imMsgBody.setAppId(BizEnum.LIVE_BIZ.getCode());
                imMsgBody.setToken(token);
                IMMsg reqMsg = IMMsg.build(IMMsgCodeEnum.IM_LOGIN_MSG.getCode(), JSON.toJSONString(imMsgBody));
                channel.writeAndFlush(reqMsg);

            }
        }).start();
    }
}
