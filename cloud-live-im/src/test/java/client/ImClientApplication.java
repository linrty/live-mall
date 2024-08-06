package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import top.linrty.live.common.enums.im.IMMsgCodeEnum;
import top.linrty.live.common.domain.po.im.IMMsg;
import top.linrty.live.im.utils.IMMsgDecoder;
import top.linrty.live.im.utils.IMMsgEncoder;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/31 23:10
 * @Version: 1.0
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class ImClientApplication {
    private void startConnection(String address, int port) throws InterruptedException {
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
        ChannelFuture channelFuture = bootstrap.connect(address, port).sync();
        Channel channel = channelFuture.channel();
        for (int i = 0; i < 100; i++) {
            channel.writeAndFlush(IMMsg.build(IMMsgCodeEnum.IM_LOGIN_MSG.getCode(), "login"));
            channel.writeAndFlush(IMMsg.build(IMMsgCodeEnum.IM_LOGOUT_MSG.getCode(), "logout"));
            channel.writeAndFlush(IMMsg.build(IMMsgCodeEnum.IM_BIZ_MSG.getCode(), "biz"));
            channel.writeAndFlush(IMMsg.build(IMMsgCodeEnum.IM_HEARTBEAT_MSG.getCode(), "heart"));
            Thread.sleep(3000);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SpringApplication springApplication = new SpringApplication(ImClientApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }
}
