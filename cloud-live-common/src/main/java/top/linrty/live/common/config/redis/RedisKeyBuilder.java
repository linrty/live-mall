package top.linrty.live.common.config.redis;

import org.springframework.beans.factory.annotation.Value;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/19 0:16
 * @Version: 1.0
 **/
public class RedisKeyBuilder {

    @Value("${spring.application.name}")
    private String applicationName;
    private static final String SPLIT_ITEM = ":";

    public String getSplitItem() {
        return SPLIT_ITEM;
    }

    public String getPrefix() {
        return applicationName + SPLIT_ITEM;
    }
}
