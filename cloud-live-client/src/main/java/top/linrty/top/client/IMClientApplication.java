package top.linrty.top.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/1 21:52
 * @Version: 1.0
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class IMClientApplication {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication springApplication = new SpringApplication(IMClientApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }
}
