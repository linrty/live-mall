package top.linrty.live.msg.handler;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 23:38
 * @Version: 1.0
 **/
@SpringBootApplication
@EnableDubbo
@EnableDiscoveryClient
public class MsgApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsgApplication.class, args);
    }
}
