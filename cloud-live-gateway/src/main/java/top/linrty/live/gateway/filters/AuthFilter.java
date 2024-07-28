package top.linrty.live.gateway.filters;

import cn.hutool.core.text.AntPathMatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import top.linrty.live.common.config.auth.AuthConfig;
import top.linrty.live.common.constants.HTTPKeyConstants;
import top.linrty.live.common.utils.JwtUtils;

import java.util.List;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/28 23:17
 * @Version: 1.0
 **/
@Component
@Slf4j
@RequiredArgsConstructor
public class AuthFilter implements GlobalFilter, Ordered {

    private final AuthConfig authConfig;

    private final JwtUtils jwtUtils;

    private final AntPathMatcher antPathMatcher;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!authConfig.isEnabled()){
            return chain.filter(exchange);
        }

        //获取request
        ServerHttpRequest request = exchange.getRequest();
        //判断请求是否要拦截
        if (isAllowPath(request)){
            return chain.filter(exchange);
        }

        // 获取token
        String token=null;
        List<String> headers = request.getHeaders().get(HTTPKeyConstants.HTTP_HEADER_AUTHORIZATION);
        if (headers!=null){
            token = headers.get(0);
        }
        //要拦截,解析token
        String userId =null;
        try {
            userId = jwtUtils.getUserIdByToken(token);
            log.info("==========================userId=========================" + userId);
        }catch (Exception e){
            ServerHttpResponse response = exchange.getResponse();
            response.setRawStatusCode(HttpStatus.UNAUTHORIZED.value());
            return response.setComplete();
        }
        if (userId==null){
            ServerHttpResponse response = exchange.getResponse();
            response.setRawStatusCode(HttpStatus.UNAUTHORIZED.value());
            return response.setComplete();
        }
        //传递用户到服务
        String userInfo = userId;
        ServerWebExchange exc = exchange.mutate()
                .request(builder -> builder.header(HTTPKeyConstants.HTTP_HEADER_USER_INFO, userInfo))
                .build();
        //放行
        return chain.filter(exc);
    }


    @Override
    public int getOrder() {
        return 0;
    }

    private boolean isAllowPath(ServerHttpRequest request) {
        boolean flag=false;
        // String method=request.getMethodValue();
        String path=request.getPath().toString();
        for (String excludePath : authConfig.getExcludePaths()) {
            if (antPathMatcher.match(excludePath,path)){
                flag=true;
                break;
            }
        }
        return flag;
    }
}
