package top.linrty.live.living.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import top.linrty.live.common.constants.HTTPKeyConstants;
import top.linrty.live.common.constants.living.GiftTopicNames;
import top.linrty.live.common.domain.dto.living.GiftConfigDTO;
import top.linrty.live.common.domain.dto.living.GiftDTO;
import top.linrty.live.common.domain.dto.living.GiftReqDTO;
import top.linrty.live.common.domain.vo.living.GiftConfigVO;
import top.linrty.live.common.exception.UnknownException;
import top.linrty.live.common.utils.ConvertBeanUtils;
import top.linrty.live.common.utils.RequestContext;
import top.linrty.live.living.service.IGiftConfigService;
import top.linrty.live.living.service.IGiftService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 0:30
 * @Version: 1.0
 **/
@Service
@Slf4j
public class GiftServiceImpl implements IGiftService {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Resource
    private IGiftConfigService giftConfigService;


    @Override
    public boolean send(GiftReqDTO giftReqDTO) {
        int giftId = giftReqDTO.getGiftId();
        // 查询本地缓存
        GiftConfigDTO giftConfigDTO = BeanUtil.copyProperties(giftConfigService.getByGiftId(giftId), GiftConfigDTO.class);
        if (giftConfigDTO == null) {
            throw new UnknownException("礼物信息异常");
        }
        if (!giftReqDTO.getReceiverId().equals(giftReqDTO.getSenderUserId())) {
            throw new UnknownException("目前正有人连线，不能给自己送礼");
        }
        // 进行异步消费
        GiftDTO giftDTO = new GiftDTO();
        giftDTO.setUserId(Long.parseLong(RequestContext.get(HTTPKeyConstants.HTTP_HEADER_USER_ID).toString()));
        giftDTO.setGiftId(giftId);
        giftDTO.setRoomId(giftReqDTO.getRoomId());
        giftDTO.setReceiverId(giftReqDTO.getReceiverId());
        giftDTO.setPrice(giftConfigDTO.getPrice());
        giftDTO.setUrl(giftConfigDTO.getSvgaUrl());
        giftDTO.setType(giftReqDTO.getType());
        // 设置唯一标识UUID，防止重复消费
        giftDTO.setUuid(UUID.randomUUID().toString());
        CompletableFuture<SendResult<String, String>> sendResult = kafkaTemplate.send(
                GiftTopicNames.SEND_GIFT,
                // giftReqVO.getRoomId().toString(), //指定key，将相同roomId的送礼消息发送到一个分区，避免PK送礼消息出现乱序
                JSON.toJSONString(giftDTO)
        );
        sendResult.whenComplete((v, e) -> {
            if (e == null) {
                log.info("[gift-send] send result is {}", sendResult);
            }
        }).exceptionally(e -> {
            log.info("[gift-send] send result is error:", e);
            return null;
        });
        // 同步消费逻辑
        // AccountTradeReqDTO accountTradeReqDTO = new AccountTradeReqDTO();
        // accountTradeReqDTO.setUserId(QiyuRequestContext.getUserId());
        // accountTradeReqDTO.setNum(giftConfigDTO.getPrice());
        // AccountTradeRespDTO tradeRespDTO = qiyuCurrencyAccountRpc.consumeForSendGift(accountTradeReqDTO);
        // ErrorAssert.isTure(tradeRespDTO != null && tradeRespDTO.isSuccess(), ApiErrorEnum.SEND_GIFT_ERROR);
        return true;
    }
}
