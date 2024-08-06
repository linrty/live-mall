package top.linrty.live.im.handler.impl;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.RedisTemplate;
import top.linrty.live.api.clients.IMClient;
import top.linrty.live.common.domain.po.im.IMMsgBody;
import top.linrty.live.im.service.IRouterHandlerService;
import top.linrty.live.im.utils.IMProviderCacheKeyBuilder;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/4 13:09
 * @Version: 1.0
 **/
@DubboService
public class IMClientImpl implements IMClient {

    @Resource
    private IRouterHandlerService routerHandlerService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private IMProviderCacheKeyBuilder imProviderCacheKeyBuilder;

    @Override
    public void sendMsg(IMMsgBody imMsgBody) {
        routerHandlerService.onReceive(imMsgBody);
    }

    @Override
    public boolean onlineCheck(Long userId, Integer appId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(imProviderCacheKeyBuilder.buildIMBindIpKey(userId, appId)));
    }
}
