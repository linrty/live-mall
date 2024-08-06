package top.linrty.live.im.service.impl;

import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import top.linrty.live.common.constants.im.IMMsgTopicNames;
import top.linrty.live.common.domain.po.im.IMMsg;
import top.linrty.live.common.domain.po.im.IMMsgBody;
import top.linrty.live.common.enums.im.IMMsgCodeEnum;
import top.linrty.live.common.utils.RedisSeqIdHelper;
import top.linrty.live.im.cache.ChannelCache;
import top.linrty.live.im.service.IAckMsgService;
import top.linrty.live.im.service.IRouterHandlerService;
import top.linrty.live.im.utils.IMProviderCacheKeyBuilder;

import java.util.concurrent.CompletableFuture;

/**
 * @Description: 路由消息处理服务实现类
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/3 15:46
 * @Version: 1.0
 **/
@Service
@Slf4j
public class RouterHandlerServiceImpl implements IRouterHandlerService {

    @Resource
    private IMProviderCacheKeyBuilder imProviderCacheKeyBuilder;

    @Resource
    private RedisSeqIdHelper redisSeqIdHelper;

    @Resource
    private IAckMsgService ackMsgService;

    @Override
    public void onReceive(IMMsgBody imMsgBody) {
        Long msgId = redisSeqIdHelper.nextId(imProviderCacheKeyBuilder.buildIMAckMsgIdKey());
        imMsgBody.setMsgId(msgId.toString());
        if (sendMsgToClient(imMsgBody)){
            ackMsgService.recordMsgAck(imMsgBody, 1);
            ackMsgService.sendDelayMsg(imMsgBody);
        }
    }

    @Override
    public boolean sendMsgToClient(IMMsgBody imMsgBody) {
        // 需要进行消息通知的userId
        Long userId = imMsgBody.getUserId();
        ChannelHandlerContext ctx = ChannelCache.get(userId);
        if (ctx != null) {
            IMMsg respMsg = IMMsg.build(IMMsgCodeEnum.IM_BIZ_MSG.getCode(), JSON.toJSONString(imMsgBody));
            ctx.writeAndFlush(respMsg);
            return true;
        }
        return false;
    }
}
