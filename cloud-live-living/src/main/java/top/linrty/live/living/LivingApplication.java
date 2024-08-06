package top.linrty.live.living;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/4 23:25
 * @Version: 1.0
 **/
@SpringBootApplication
@EnableDubbo
@EnableDiscoveryClient
public class LivingApplication {
    public static void main(String[] args) {

    }
}
