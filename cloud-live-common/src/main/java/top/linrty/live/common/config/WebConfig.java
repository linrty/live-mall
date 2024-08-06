package top.linrty.live.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.linrty.live.common.config.handler.RequestLimitInterceptor;
import top.linrty.live.common.config.handler.UserInfoInterceptor;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/30 16:00
 * @Version: 1.0
 **/
@Configuration
public class WebConfig implements WebMvcConfigurer{

    @Bean
    public UserInfoInterceptor userInfoInterceptor(){
        return new UserInfoInterceptor();
    }

    @Bean
    public RequestLimitInterceptor requestLimitInterceptor(){
        return new RequestLimitInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInfoInterceptor());
        registry.addInterceptor(requestLimitInterceptor());
    }

}
