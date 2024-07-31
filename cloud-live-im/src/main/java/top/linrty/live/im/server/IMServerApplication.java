package top.linrty.live.im.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import top.linrty.live.im.handler.IMServerHandler;
import top.linrty.live.im.utils.ImMsgDecoder;
import top.linrty.live.im.utils.ImMsgEncoder;

/**
 * @Description: 基于Netty的IM启动类
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/31 20:13
 * @Version: 1.0
 **/
@Slf4j
@Data
public class IMServerApplication {

    // 监听的端口
    private int port;

    public void start(int port) throws InterruptedException {
        this.port = port;
        // accept
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        // read/write
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        //netty初始化相关的handler
        serverBootstrap.childHandler(new ChannelInitializer<>() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                //打印日志，方便观察
                log.info("初始化连接渠道");
                //设计消息体ImMsg
                //添加编解码器
                channel.pipeline().addLast(new ImMsgEncoder());
                channel.pipeline().addLast(new ImMsgDecoder());
                //设置这个netty处理handler
                channel.pipeline().addLast(new IMServerHandler());
            }
        });

        //基于JVM的钩子函数去实现优雅关闭
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }));

        ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
        log.info("Netty服务启动成功，监听端口为{}", getPort());
        //这里会阻塞主线程，实现服务长期开启的效果
        channelFuture.channel().closeFuture().sync();
    }

    public static void main(String[] args) throws InterruptedException {
        IMServerApplication nettyImServerApplication = new IMServerApplication();
        nettyImServerApplication.start(9090);
    }

}
