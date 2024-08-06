package top.linrty.live.router.service.impl;

import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.linrty.live.api.clients.RouterHandlerClient;
import top.linrty.live.common.domain.po.im.IMMsgBody;
import top.linrty.live.router.service.IRouterService;
import top.linrty.live.router.utils.RouterProviderCacheKeyBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/3 16:30
 * @Version: 1.0
 **/
@Service
public class RouterServiceImpl implements IRouterService {


    @DubboReference
    private RouterHandlerClient routerHandlerClient;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private RouterProviderCacheKeyBuilder routerProviderCacheKeyBuilder;


    @Override
    public boolean sendMsg(IMMsgBody imMsgBody) {
        String bindAddress = redisTemplate.opsForValue().get(routerProviderCacheKeyBuilder.buildIMBindIpKey(imMsgBody.getUserId(), imMsgBody.getAppId()));
        if (StrUtil.isEmpty(bindAddress)) {
            return false;
        }
        bindAddress = bindAddress.substring(0, bindAddress.indexOf("%"));//新加的：去除后面拼接的userId
        RpcContext.getContext().set("ip", bindAddress);
        routerHandlerClient.sendMsg(imMsgBody);
        return true;
    }

    @Override
    public void batchSendMsg(List<IMMsgBody> imMsgBodyList) {
//我们需要对IP进行分组，对相同IP服务器的userIdList进行分组，每组进行一此调用，减少网络开销
        String cacheKeyPrefix = routerProviderCacheKeyBuilder.buildIMBindIPKeyPrefix(imMsgBodyList.get(0).getAppId());
        List<String> cacheKeyList = imMsgBodyList.stream().map(IMMsgBody::getUserId).map(userId -> cacheKeyPrefix + userId).collect(Collectors.toList());
        //批量去除每个用户绑定的ip地址
        List<String> ipList = redisTemplate.opsForValue().multiGet(cacheKeyList);
        Map<String, List<Long>> userIdMap = new HashMap<>();
        ipList.forEach(ip -> {
            String currentIp = ip.substring(0, ip.indexOf("%"));
            Long userId = Long.valueOf(ip.substring(ip.indexOf("%") + 1));

            List<Long> currentUserIdList = userIdMap.getOrDefault(currentIp, new ArrayList<Long>());
            currentUserIdList.add(userId);
            userIdMap.put(currentIp, currentUserIdList);
        });
        //根据注册IP对ImMsgBody进行分组
        //将连接到同一台ip地址的ImMsgBody组装到一个List中，进行统一发送
        Map<Long, IMMsgBody> userIdMsgMap = imMsgBodyList.stream().collect(Collectors.toMap(IMMsgBody::getUserId, body -> body));
        for (Map.Entry<String, List<Long>> entry : userIdMap.entrySet()) {
            //设置dubbo RPC上下文
            RpcContext.getContext().set("ip", entry.getKey());
            List<Long> currentUserIdList = entry.getValue();
            List<IMMsgBody> batchSendMsgBodyGroupByIpList = currentUserIdList.stream().map(userIdMsgMap::get).collect(Collectors.toList());
            routerHandlerClient.batchSendMsg(batchSendMsgBodyGroupByIpList);
        }
    }
}
