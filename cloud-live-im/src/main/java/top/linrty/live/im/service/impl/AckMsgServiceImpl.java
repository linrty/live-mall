package top.linrty.live.im.service.impl;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import top.linrty.live.common.constants.im.IMMsgTopicNames;
import top.linrty.live.common.domain.po.im.IMMsgBody;
import top.linrty.live.im.service.IAckMsgService;
import top.linrty.live.im.utils.IMProviderCacheKeyBuilder;

import java.util.concurrent.CompletableFuture;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/4 19:20
 * @Version: 1.0
 **/
@Service
@Slf4j
public class AckMsgServiceImpl implements IAckMsgService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private IMProviderCacheKeyBuilder imProviderCacheKeyBuilder;

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;


    @Override
    public void doMsgAck(IMMsgBody imMsgBody) {
        redisTemplate.opsForHash().delete(
                imProviderCacheKeyBuilder.buildIMAckMapKey(imMsgBody.getUserId(), imMsgBody.getAppId())
                , imMsgBody.getMsgId());
    }

    @Override
    public void recordMsgAck(IMMsgBody imMsgBody, int times) {
        redisTemplate.opsForHash().put(
                imProviderCacheKeyBuilder.buildIMAckMapKey(imMsgBody.getUserId(), imMsgBody.getAppId())
                , imMsgBody.getMsgId(), times);
    }

    @Override
    public void sendDelayMsg(IMMsgBody imMsgBody) {
        String imMsgBodyJson = JSON.toJSONString(imMsgBody);
        CompletableFuture<SendResult<String, String>> sendResultCompletableFuture = kafkaTemplate.send(IMMsgTopicNames.IM_ACK_MSG_TOPIC, imMsgBodyJson);
        sendResultCompletableFuture.whenComplete((v, e) -> {
            if (e == null) {
                log.info("[BizImMsgHandler]消息投递成功, sendResult is {}", v);
            }
        }).exceptionally(e -> {
            log.error("send error, error is :", e);
            throw new RuntimeException(e);
        });
    }

    @Override
    public int getMsgAckTimes(String msgId, Long userId, int appId) {
        Object times = redisTemplate.opsForHash().get(imProviderCacheKeyBuilder.buildIMAckMapKey(userId, appId), msgId);
        return times == null ? -1 : (int) times;
    }
}
