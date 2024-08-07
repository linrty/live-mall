package top.linrty.live.living.utils;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import top.linrty.live.common.constants.living.GiftTopicNames;
import top.linrty.live.common.domain.dto.living.SendRedPacketDTO;
import top.linrty.live.living.service.IRedPacketConfigService;

/**
 * @Description: 处理抢红包mq消息的消费者
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 16:15
 * @Version: 1.0
 **/
@Component
@Slf4j
public class RedPacketConsumer {


    @Resource
    private IRedPacketConfigService redPacketConfigService;

    @KafkaListener(topics = GiftTopicNames.RECEIVE_RED_PACKET, groupId = "receive-red-packet")
    public void receiveRedPacket(String sendRedPacketBOStr) {
        try {
            SendRedPacketDTO sendRedPacketDTO = JSON.parseObject(sendRedPacketBOStr, SendRedPacketDTO.class);
            redPacketConfigService.receiveRedPacketHandler(sendRedPacketDTO.getReqDTO(), sendRedPacketDTO.getPrice());
            log.info("[ReceiveRedPacketConsumer] receiveRedPacket success");
        } catch (Exception e) {
            log.error("[ReceiveRedPacketConsumer] receiveRedPacket error, mqBody is {}", sendRedPacketBOStr);
        }
    }
}
