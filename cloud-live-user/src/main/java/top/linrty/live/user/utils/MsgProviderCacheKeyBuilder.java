package top.linrty.live.user.utils;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import top.linrty.live.common.config.redis.RedisKeyBuilder;
import top.linrty.live.common.config.redis.RedisKeyLoadMatch;

import static top.linrty.live.common.constants.RedisPrefixKey.USER_LOGIN_CODE_KEY;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/28 21:41
 * @Version: 1.0
 **/
@Configuration
@Conditional(RedisKeyLoadMatch.class)
public class MsgProviderCacheKeyBuilder extends RedisKeyBuilder {

    public String buildSmsLoginCodeKey(String phone){
        return super.getPrefix() + USER_LOGIN_CODE_KEY + super.getSplitItem() + phone;
    }
}
