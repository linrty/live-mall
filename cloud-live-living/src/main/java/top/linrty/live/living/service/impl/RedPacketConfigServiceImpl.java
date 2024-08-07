package top.linrty.live.living.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import top.linrty.live.api.clients.PayClient;
import top.linrty.live.api.clients.RouterClient;
import top.linrty.live.common.constants.living.GiftTopicNames;
import top.linrty.live.common.domain.dto.living.LivingRoomReqDTO;
import top.linrty.live.common.domain.dto.living.RedPacketConfigReqDTO;
import top.linrty.live.common.domain.dto.living.RedPacketReceiveDTO;
import top.linrty.live.common.domain.dto.living.SendRedPacketDTO;
import top.linrty.live.common.domain.po.im.IMMsgBody;
import top.linrty.live.common.enums.BizEnum;
import top.linrty.live.common.enums.im.IMMsgBizCodeEnum;
import top.linrty.live.common.enums.living.RedPacketStatusEnum;
import top.linrty.live.common.exception.UnknownException;
import top.linrty.live.common.utils.ListUtils;
import top.linrty.live.living.domain.po.RedPacketConfig;
import top.linrty.live.living.mapper.RedPacketConfigMapper;
import top.linrty.live.living.service.ILivingRoomService;
import top.linrty.live.living.service.IRedPacketConfigService;
import top.linrty.live.living.utils.GiftProviderCacheKeyBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 15:39
 * @Version: 1.0
 **/
@Service
@Slf4j
public class RedPacketConfigServiceImpl implements IRedPacketConfigService {

    @Resource
    private RedPacketConfigMapper redPacketConfigMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private GiftProviderCacheKeyBuilder giftProviderCacheKeyBuilder;

    @Resource
    private RedissonClient redissonClient;

    @DubboReference
    private RouterClient routerClient;

    @Resource
    private ILivingRoomService livingRoomService;

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @DubboReference
    private PayClient payClient;

    @Override
    public RedPacketConfig queryByAnchorId(Long anchorId) {
        LambdaQueryWrapper<RedPacketConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RedPacketConfig::getAnchorId, anchorId);
        queryWrapper.eq(RedPacketConfig::getStatus, RedPacketStatusEnum.WAIT.getCode());
        queryWrapper.orderByDesc(RedPacketConfig::getCreateTime);
        queryWrapper.last("limit 1");
        return redPacketConfigMapper.selectOne(queryWrapper);
    }

    @Override
    public RedPacketConfig queryByConfigCode(String code) {
        LambdaQueryWrapper<RedPacketConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RedPacketConfig::getConfigCode, code);
        queryWrapper.eq(RedPacketConfig::getStatus, RedPacketStatusEnum.IS_PREPARED.getCode());
        queryWrapper.orderByDesc(RedPacketConfig::getCreateTime);
        queryWrapper.last("limit 1");
        return redPacketConfigMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean addOne(RedPacketConfig redPacketConfig) {
        redPacketConfig.setConfigCode(UUID.randomUUID().toString());
        return redPacketConfigMapper.insert(redPacketConfig) > 0;
    }

    @Override
    public boolean updateById(RedPacketConfig redPacketConfig) {
        return redPacketConfigMapper.updateById(redPacketConfig) > 0;
    }

    @Override
    public boolean prepareRedPacket(Long anchorId) {
        // 防止重复生成，以及错误参数传递情况
        RedPacketConfig redPacketConfig = this.queryByAnchorId(anchorId);
        if (redPacketConfig == null) {
            return false;
        }
        String lockKey = giftProviderCacheKeyBuilder.buildRedPacketInitLock(redPacketConfig.getConfigCode());
        RLock lock = redissonClient.getLock(lockKey);
        try {
            Boolean isLock = lock.tryLock(1, 3L, TimeUnit.SECONDS);
            if (Boolean.FALSE.equals(isLock)) {
                return false;
            }
            Integer totalPrice = redPacketConfig.getTotalPrice();
            Integer totalCount = redPacketConfig.getTotalCount();
            List<Integer> priceList = this.createRedPacketPriceList(totalPrice, totalCount);
            String cacheKey = giftProviderCacheKeyBuilder.buildRedPacketList(redPacketConfig.getConfigCode());
            // 将红包数据拆分为子集合进行插入到Redis，避免 Redis输入输出缓冲区 被填满
            List<List<Integer>> splitPriceList = ListUtils.splistList(priceList, 100);
            for (List<Integer> priceItemList : splitPriceList) {
                redisTemplate.opsForList().leftPushAll(cacheKey, priceItemList.toArray());
            }
            // 更改红包雨配置状态，防止重发
            redPacketConfig.setStatus(RedPacketStatusEnum.IS_PREPARED.getCode());
            this.updateById(redPacketConfig);
            // Redis中设置该红包雨已经准备好的标记
            redisTemplate.opsForValue().set(giftProviderCacheKeyBuilder.buildRedPacketPrepareSuccess(redPacketConfig.getConfigCode()), 1, 1L, TimeUnit.DAYS);
        }catch (Exception e){
            log.error("[RedPacketConfigServiceImpl.prepareRedPacket] 获取锁失败", e);
            throw new UnknownException("获取信息失败");
        } finally {
            lock.unlock();
        }
        return true;
    }

    /**
     * 二倍均值法：
     * 创建红包雨的每个红包金额数据
     */
    private List<Integer> createRedPacketPriceList(Integer totalPrice, Integer totalCount) {
        List<Integer> redPacketPriceList = new ArrayList<>();
        for (int i = 0; i < totalCount; i++) {
            if (i + 1 == totalCount) {
                // 如果是最后一个红包
                redPacketPriceList.add(totalPrice);
                break;
            }
            int maxLimit = (totalPrice / (totalCount - i)) * 2;// 最大限额为平均值的两倍
            int currentPrice = ThreadLocalRandom.current().nextInt(1, maxLimit);
            totalPrice -= currentPrice;
            redPacketPriceList.add(currentPrice);
        }
        return redPacketPriceList;
    }

    @Override
    public RedPacketReceiveDTO receiveRedPacket(RedPacketConfigReqDTO redPacketConfigReqDTO) {
        String code = redPacketConfigReqDTO.getRedPacketConfigCode();
        // 从Redis中领取一个红包金额
        String cacheKey = giftProviderCacheKeyBuilder.buildRedPacketList(code);
        Object priceObj = redisTemplate.opsForList().rightPop(cacheKey);
        if (priceObj == null) {
            return null;
        }
        Integer price = (Integer) priceObj;
        // 发送mq消息进行异步信息的统计，以及用户余额的增加
        SendRedPacketDTO sendRedPacketDTO = new SendRedPacketDTO();
        sendRedPacketDTO.setPrice(price);
        sendRedPacketDTO.setReqDTO(redPacketConfigReqDTO);
        CompletableFuture<SendResult<String, String>> sendResult = kafkaTemplate.send(GiftTopicNames.RECEIVE_RED_PACKET, JSON.toJSONString(sendRedPacketDTO));
        try {
            sendResult.whenComplete((v, e) -> {
                if (e == null) {
                    log.info("[RedPacketConfigServiceImpl] user {} receive a redPacket, send success", redPacketConfigReqDTO.getUserId());
                }
            }).exceptionally(e -> {
                log.error("[RedPacketConfigServiceImpl] send error, userId is {}, price is {}", redPacketConfigReqDTO.getUserId(), price);
                throw new RuntimeException(e);
            });
        } catch (Exception e) {
            return new RedPacketReceiveDTO().setNotifyMsg("抱歉，红包被人抢走了，再试试");
        }
        return new RedPacketReceiveDTO().setPrice(price).setNotifyMsg("恭喜领取到红包：" + price + "虚拟币！");
    }

    @Override
    public Boolean startRedPacket(RedPacketConfigReqDTO reqDTO) {
        String code = reqDTO.getRedPacketConfigCode();
        // 红包没有准备好，则返回false
        if (Boolean.FALSE.equals(redisTemplate.hasKey(giftProviderCacheKeyBuilder.buildRedPacketPrepareSuccess(code)))) {
            return false;
        }
        // 红包已经开始过（有别的线程正在通知用户中），返回false
        String notifySuccessCacheKey = giftProviderCacheKeyBuilder.buildRedPacketNotify(code);
        if (Boolean.TRUE.equals(redisTemplate.hasKey(notifySuccessCacheKey))) {
            return false;
        }
        redisTemplate.opsForValue().set(notifySuccessCacheKey, 1, 1L, TimeUnit.DAYS);
        // 广播通知直播间所有用户开始抢红包了
        RedPacketConfig redPacketConfig = this.queryByConfigCode(code);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("redPacketConfig", JSON.toJSONString(redPacketConfig));
        LivingRoomReqDTO livingRoomReqDTO = new LivingRoomReqDTO();
        livingRoomReqDTO.setRoomId(reqDTO.getRoomId());
        livingRoomReqDTO.setAppId(BizEnum.LIVE_BIZ.getCode());
        List<Long> userIdList = livingRoomService.queryUserIdsByRoomId(livingRoomReqDTO);
        if (CollectionUtil.isEmpty(userIdList)) return false;
        this.batchSendImMsg(userIdList, IMMsgBizCodeEnum.RED_PACKET_CONFIG.getCode(), jsonObject);
        // 更改红包雨配置的状态为已发送
        redPacketConfig.setStatus(RedPacketStatusEnum.IS_SEND.getCode());
        this.updateById(redPacketConfig);
        return true;
    }

    @Override
    public void receiveRedPacketHandler(RedPacketConfigReqDTO reqDTO, Integer price) {
        String code = reqDTO.getRedPacketConfigCode();
        String totalGetCountCacheKey = giftProviderCacheKeyBuilder.buildRedPacketTotalGetCount(code);
        String totalGetPriceCacheKey = giftProviderCacheKeyBuilder.buildRedPacketTotalGetPrice(code);
        // 记录该用户总共领取了多少金额的红包
        redisTemplate.opsForValue().increment(giftProviderCacheKeyBuilder.buildUserTotalGetPrice(reqDTO.getUserId()), price);
        redisTemplate.opsForHash().increment(totalGetCountCacheKey, code, 1);
        redisTemplate.expire(totalGetCountCacheKey, 1L, TimeUnit.DAYS);
        redisTemplate.opsForHash().increment(totalGetPriceCacheKey, code, price);
        redisTemplate.expire(totalGetPriceCacheKey, 1L, TimeUnit.DAYS);
        // 往用户的余额里增加金额
        payClient.incrCurrencyAccount(reqDTO.getUserId(), price);
        // 持久化红包雨的totalGetCount和totalGetPrice
        redPacketConfigMapper.incrTotalGetPrice(code, price);
        redPacketConfigMapper.incrTotalGetCount(code);
    }

    /**
     * 批量发送im消息
     */
    private void batchSendImMsg(List<Long> userIdList, Integer bizCode, JSONObject jsonObject) {
        List<IMMsgBody> imMsgBodies = new ArrayList<>();

        userIdList.forEach(userId -> {
            IMMsgBody imMsgBody = new IMMsgBody();
            imMsgBody.setAppId(BizEnum.LIVE_BIZ.getCode());
            imMsgBody.setBizCode(bizCode);
            imMsgBody.setData(jsonObject.toJSONString());
            imMsgBody.setUserId(userId);
            imMsgBodies.add(imMsgBody);
        });
        routerClient.batchSendMsg(imMsgBodies);
    }
}
