package top.linrty.live.living.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import top.linrty.live.api.clients.PayClient;
import top.linrty.live.api.clients.RouterClient;
import top.linrty.live.common.constants.living.GiftTopicNames;
import top.linrty.live.common.domain.dto.living.GiftDTO;
import top.linrty.live.common.domain.dto.pay.AccountTradeReqDTO;
import top.linrty.live.common.domain.dto.pay.AccountTradeRespDTO;
import top.linrty.live.common.domain.po.im.IMMsgBody;
import top.linrty.live.common.enums.BizEnum;
import top.linrty.live.common.enums.im.IMMsgBizCodeEnum;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 0:55
 * @Version: 1.0
 **/
@Component
@Slf4j
public class GiftConsumer {
    @DubboReference
    private PayClient payClient;

    @Resource
    private GiftProviderCacheKeyBuilder giftProviderCacheKeyBuilder;

    @DubboReference
    private RouterClient routerClient;

    @Resource
    private RedissonClient redissonClient;

    @KafkaListener(topics = GiftTopicNames.SEND_GIFT, groupId = "send-gift-consumer")
    public void consumeSendGift(List<ConsumerRecord<?, ?>> records) {
        // 批量拉取消息进行处理
        for (ConsumerRecord<?, ?> record : records) {
            String sendGiftMqStr = (String) record.value();
            GiftDTO giftDTO = JSON.parseObject(sendGiftMqStr, GiftDTO.class);
            String mqConsumerKey = giftProviderCacheKeyBuilder.buildGiftConsumeKey(giftDTO.getUuid());
            RLock lock = redissonClient.getLock(mqConsumerKey);
            try {
                Boolean isLock = lock.tryLock(1, 5L, TimeUnit.SECONDS);
                if (Boolean.FALSE.equals(isLock)) {
                    // 代表曾经消费过，防止重复消费
                    continue;
                }
                Long userId = giftDTO.getUserId();
                AccountTradeReqDTO accountTradeReqDTO = new AccountTradeReqDTO();
                accountTradeReqDTO.setUserId(userId);
                accountTradeReqDTO.setNum(giftDTO.getPrice());
                AccountTradeRespDTO tradeRespDTO = payClient.consumeForSendGift(accountTradeReqDTO);

                // 如果余额扣减成功
                IMMsgBody imMsgBody = new IMMsgBody();
                imMsgBody.setAppId(BizEnum.LIVE_BIZ.getCode());
                JSONObject jsonObject = new JSONObject();
                if (tradeRespDTO.isSuccess()) {
                    // TODO 触发礼物特效推送功能
                    imMsgBody.setBizCode(IMMsgBizCodeEnum.LIVING_ROOM_SEND_GIFT_SUCCESS.getCode());
                    imMsgBody.setUserId(giftDTO.getReceiverId());// 传达给接收者
                    jsonObject.put("url", giftDTO.getUrl());
                    log.info("[GiftConsumer] send success, msg is {}", record);
                } else {
                    // TODO 利用IM将发送失败的消息告知用户
                    imMsgBody.setBizCode(IMMsgBizCodeEnum.LIVING_ROOM_SEND_GIFT_FAIL.getCode());
                    imMsgBody.setUserId(userId);// 失败信息只传达给发送者
                    jsonObject.put("msg", tradeRespDTO.getMsg());
                    log.info("[GiftConsumer] send fail, msg is {}", tradeRespDTO.getMsg());
                }
                imMsgBody.setData(jsonObject.toJSONString());
                routerClient.sendMsg(imMsgBody);
            }catch (Exception e){

            } finally {
                lock.unlock();
            }
        }
    }
}
