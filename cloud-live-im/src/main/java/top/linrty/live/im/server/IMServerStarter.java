package top.linrty.live.im.server;

import cn.hutool.core.util.StrUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.linrty.live.im.cache.ChannelCache;
import top.linrty.live.im.handler.IMServerHandler;
import top.linrty.live.im.utils.IMMsgDecoder;
import top.linrty.live.im.utils.IMMsgEncoder;

import java.net.InetAddress;

/**
 * @Description: 基于Netty的IM启动类
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/31 20:13
 * @Version: 1.0
 **/
@Slf4j
@Service
public class IMServerStarter implements InitializingBean {

    // 监听的端口
    @Value("${live.im.port:}")
    private int port;

    // netty服务IP,用于路由
    @Value("${live.im.host:}")
    private String host;

    @Resource
    private IMServerHandler imServerHandler;

    public void start() throws InterruptedException {
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
                channel.pipeline().addLast(new IMMsgEncoder());
                channel.pipeline().addLast(new IMMsgDecoder());
                //设置这个netty处理handler
                channel.pipeline().addLast(new IMServerHandler());
            }
        });

        //基于JVM的钩子函数去实现优雅关闭
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }));

        // 将IP和端口保存到cache中
        ChannelCache.setServerIp(host + ":" + port);
        ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
        log.info("Netty服务启动成功，监听端口为{}", port);
        //这里会阻塞主线程，实现服务长期开启的效果
        channelFuture.channel().closeFuture().sync();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    start();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "live-im-server").start();
    }

}
