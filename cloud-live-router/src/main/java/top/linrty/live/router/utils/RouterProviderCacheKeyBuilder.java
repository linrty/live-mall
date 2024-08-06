package top.linrty.live.router.utils;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import top.linrty.live.common.config.redis.RedisKeyBuilder;
import top.linrty.live.common.config.redis.RedisKeyLoadMatch;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/6 17:33
 * @Version: 1.0
 **/
@Component
@Conditional(RedisKeyLoadMatch.class)
public class RouterProviderCacheKeyBuilder extends RedisKeyBuilder {
    private static final String IM_BIND_IP_KEY = "imBindIpKey";

    public String buildIMBindIpKey(Long userId, Integer appId) {
        return IM_BIND_IP_KEY + getSplitItem() + appId + getSplitItem() + userId;
    }

    public String buildIMBindIPKeyPrefix(Integer appId){
        return IM_BIND_IP_KEY + getSplitItem() + appId + getSplitItem();
    }
}
