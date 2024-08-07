package top.linrty.live.living.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import top.linrty.live.api.clients.PayClient;
import top.linrty.live.api.clients.RouterClient;
import top.linrty.live.common.constants.living.GiftTopicNames;
import top.linrty.live.common.domain.dto.living.GiftDTO;
import top.linrty.live.common.domain.dto.living.GiftReqDTO;
import top.linrty.live.common.domain.dto.living.LivingRoomReqDTO;
import top.linrty.live.common.domain.dto.living.LivingRoomRespDTO;
import top.linrty.live.common.domain.dto.pay.AccountTradeReqDTO;
import top.linrty.live.common.domain.dto.pay.AccountTradeRespDTO;
import top.linrty.live.common.domain.po.im.IMMsgBody;
import top.linrty.live.common.enums.BizEnum;
import top.linrty.live.common.enums.im.IMMsgBizCodeEnum;
import top.linrty.live.common.enums.living.GiftTypeEnum;
import top.linrty.live.living.service.ILivingRoomService;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    @Resource
    private ILivingRoomService livingRoomService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static final long PK_MIN_NUM = 0;
    private static final long PK_MAX_NUM = 1000;

    private static final Long PK_INIT_NUM = 50L;


    private static final DefaultRedisScript<Long> redisScript;

    static {
        redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Long.class);
        redisScript.setLocation(new ClassPathResource("getPkNumAndSeqId.lua"));
    }

    @KafkaListener(topics = GiftTopicNames.SEND_GIFT, groupId = "send-gift-consumer" , containerFactory = "batchFactory")
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
                Integer type = giftDTO.getType();
                if (tradeRespDTO.isSuccess()) {
                    // 如果余额扣减成功
                    // 0 查询在直播间的userId
                    LivingRoomReqDTO livingRoomReqDTO = new LivingRoomReqDTO();
                    livingRoomReqDTO.setRoomId(giftDTO.getRoomId())
                            .setAppId(BizEnum.LIVE_BIZ.getCode());
                    List<Long> userIdList = livingRoomService.queryUserIdsByRoomId(livingRoomReqDTO);
                    // TODO 触发礼物特效推送功能
                    if (type.equals(GiftTypeEnum.DEFAULT_SEND_GIFT.getCode())){
                        // 默认送礼，发送给全直播用户礼物特效
                        jsonObject.put("url", giftDTO.getUrl());
                        // 利用封装方法发送单播消息
                        batchSendImMsg(userIdList, IMMsgBizCodeEnum.LIVING_ROOM_SEND_GIFT_SUCCESS.getCode(), jsonObject);
                        log.info("[GiftConsumer] send success, msg is {}", record);
                    }else if(type.equals(GiftTypeEnum.PK_SEND_GIFT.getCode())){
                        // PK送礼，要求全体可见
                        // 1 礼物特效url全直播可见
                        jsonObject.put("url", giftDTO.getUrl());
                        // 2 TODO PK进度条全直播间可见
                        // 3 搜索要发送的用户
                        // 利用封装方法发送批量消息，bizCode为PK_SEND_SUCCESS
                        batchSendImMsg(userIdList, IMMsgBizCodeEnum.LIVING_ROOM_PK_SEND_GIFT_SUCCESS.getCode(), jsonObject);
                    }
                } else{
                    // 没成功，返回失败信息
                    jsonObject.put("msg", tradeRespDTO.getMsg());
                    // 利用封装方法发送单播消息，bizCode为fail类型
                    this.sendImMsgSingleton(userId, IMMsgBizCodeEnum.LIVING_ROOM_SEND_GIFT_FAIL.getCode(), jsonObject);
                    log.info("[sendGiftConsumer] send fail, msg is {}", tradeRespDTO.getMsg());
                }
            }catch (Exception e){
                log.error("[sendGiftConsumer] send fail, msg is {}", e.getMessage());
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * PK直播间送礼扣费成功后的流程：
     * 1 设置礼物特效url
     * 2 设置PK进度条的值
     * 3 批量推送给直播间全体用户
     * @param giftDTO 发送消息请求req
     * @param jsonObject 返回的ImMsgBody的data部分
     * @param roomId     直播间id
     * @param userIdList 直播间在线用户列表
     */
    private void pkIMMsgSend(GiftDTO giftDTO, JSONObject jsonObject, Integer roomId, List<Long> userIdList) {
        // PK送礼，要求全体可见
        // 1 TODO PK进度条全直播间可见

        String isOverCacheKey = giftProviderCacheKeyBuilder.buildLivingPkIsOver(roomId);
        // 1.1 判断直播PK是否已经结束
        Boolean isOver = redisTemplate.hasKey(isOverCacheKey);
        if (Boolean.TRUE.equals(isOver)) {
            return;
        }
        // 1.2 获取 pkUserId 和 pkObjId
        Long pkObjId = livingRoomService.queryOnlinePkUserId(roomId);
        LivingRoomRespDTO livingRoomRespDTO = livingRoomService.queryByRoomId(roomId);
        if (pkObjId == null || livingRoomRespDTO == null || livingRoomRespDTO.getAnchorId() == null) {
            log.error("[sendGiftConsumer] 两个用户已经有不在线的，roomId is {}", roomId);
            return;
        }
        Long pkUserId = livingRoomRespDTO.getAnchorId();
        // 1.3 获取当前进度条值 和 送礼序列号
        String pkNumKey = giftProviderCacheKeyBuilder.buildLivingPkKey(roomId);
        Long pkNum = 0L;
        // 获取该条消息的序列号，避免消息乱序
        Long sendGiftSeqNum = System.currentTimeMillis();
        if (giftDTO.getReceiverId().equals(pkUserId)) {
            Integer moveStep = giftDTO.getPrice() / 10;
            // 收礼人是房主userId，则进度条增加
            pkNum = redisTemplate.execute(redisScript, Collections.singletonList(pkNumKey), PK_INIT_NUM, PK_MAX_NUM, PK_MIN_NUM, moveStep);
            if (PK_MAX_NUM <= pkNum) {
                jsonObject.put("winnerId", pkUserId);
            }
        } else if (giftDTO.getReceiverId().equals(pkObjId)) {
            Integer moveStep = giftDTO.getPrice() / 10 * -1;
            // 收礼人是来挑战的，则进图条减少
            pkNum = redisTemplate.execute(redisScript, Collections.singletonList(pkNumKey), PK_INIT_NUM, PK_MAX_NUM, PK_MIN_NUM, moveStep);
            if (PK_MIN_NUM >= pkNum) {
                jsonObject.put("winnerId", pkObjId);
            }
        }
        jsonObject.put("receiverId", giftDTO.getReceiverId());
        jsonObject.put("sendGiftSeqNum", sendGiftSeqNum);
        jsonObject.put("pkNum", pkNum);
        // 2 礼物特效url全直播间可见
        jsonObject.put("url", giftDTO.getUrl());
        // 3 搜索要发送的用户
        // 利用封装方法发送批量消息，bizCode为PK_SEND_SUCCESS
        this.batchSendImMsg(userIdList, IMMsgBizCodeEnum.LIVING_ROOM_PK_SEND_GIFT_SUCCESS.getCode(), jsonObject);
    }

    /**
     * 单向通知送礼对象
     */
    private void sendImMsgSingleton(Long userId, Integer bizCode, JSONObject jsonObject) {
        IMMsgBody imMsgBody = new IMMsgBody();
        imMsgBody.setAppId(BizEnum.LIVE_BIZ.getCode());
        imMsgBody.setBizCode(bizCode);
        imMsgBody.setUserId(userId);
        imMsgBody.setData(jsonObject.toJSONString());
        routerClient.sendMsg(imMsgBody);
    }

    /**
     * 批量发送im消息
     */
    private void batchSendImMsg(List<Long> userIdList, Integer bizCode, JSONObject jsonObject) {
        List<IMMsgBody> imMsgBodies = userIdList.stream().map(userId -> {
            IMMsgBody imMsgBody = new IMMsgBody();
            imMsgBody.setAppId(BizEnum.LIVE_BIZ.getCode());
            imMsgBody.setBizCode(bizCode);
            imMsgBody.setData(jsonObject.toJSONString());
            imMsgBody.setUserId(userId);
            return imMsgBody;
        }).collect(Collectors.toList());
        routerClient.batchSendMsg(imMsgBodies);
    }
}
