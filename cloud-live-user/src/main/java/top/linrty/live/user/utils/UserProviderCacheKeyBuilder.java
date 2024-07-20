package top.linrty.live.user.utils;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Conditional;
import top.linrty.live.common.config.redis.RedisKeyBuilder;
import top.linrty.live.common.config.redis.RedisKeyLoadMatch;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/20 20:18
 * @Version: 1.0
 **/
@Configurable
@Conditional(RedisKeyLoadMatch.class)
public class UserProviderCacheKeyBuilder extends RedisKeyBuilder {

    private static final String USER_INFO_KEY = "userInfo";

    public String buildUserInfoKey(Long userId) {
        return super.getPrefix() + USER_INFO_KEY + super.getSplitItem() + userId;
    }
}