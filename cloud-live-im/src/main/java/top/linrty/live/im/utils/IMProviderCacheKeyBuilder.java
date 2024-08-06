package top.linrty.live.im.utils;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import top.linrty.live.common.config.redis.RedisKeyBuilder;
import top.linrty.live.common.config.redis.RedisKeyLoadMatch;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/2 23:21
 * @Version: 1.0
 **/
@Configuration
@Conditional(RedisKeyLoadMatch.class)
public class IMProviderCacheKeyBuilder extends RedisKeyBuilder {

    private static final String IM_ONLINE_ZSET = "imOnlineZset";
    private static final String IM_ACK_MAP = "imAckMap";
    private static final String IM_BIND_IP_KEY = "imBindIpKey";

    private static final String IM_ACK_MSG_ID = "imAckMsgId";

    public String buildIMAckMapKey(Long userId, Integer appId) {
        return getPrefix() + IM_ACK_MAP + getSplitItem() + userId%100 + getSplitItem() + appId;
    }

    public String buildIMLoginTokenKey(Long userId, Integer appId) {
        return getPrefix() + IM_ONLINE_ZSET + super.getSplitItem() + appId + super.getSplitItem() + userId % 10000;
    }

    public String buildIMBindIpKey(Long userId, Integer appId) {
        return IM_BIND_IP_KEY + getSplitItem() + appId + getSplitItem() + userId;
    }

    public String buildIMAckMsgIdKey() {
        return IM_ACK_MSG_ID;
    }

}
