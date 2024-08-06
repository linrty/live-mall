package top.linrty.live.living.utils;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import top.linrty.live.common.constants.im.IMMsgTopicNames;
import top.linrty.live.common.domain.dto.im.IMOfflineDTO;
import top.linrty.live.common.domain.dto.im.IMOnlineDTO;
import top.linrty.live.living.service.ILivingRoomService;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 0:56
 * @Version: 1.0
 **/
@Component
public class LivingRoomConsumer {
    @Resource
    private ILivingRoomService livingRoomService;

    @KafkaListener(topics = IMMsgTopicNames.IM_OFFLINE_TOPIC, groupId = "im-offline-consumer")
    public void consumeOffline(String imOfflineDTOStr) {
        IMOfflineDTO imOfflineDTO = JSON.parseObject(imOfflineDTOStr, IMOfflineDTO.class);
        livingRoomService.userOfflineHandler(imOfflineDTO);
    }

    @KafkaListener(topics = IMMsgTopicNames.IM_ONLINE_TOPIC, groupId = "im-online-consumer")
    public void consumeOnline(String imOnlineDTOStr) {
        IMOnlineDTO imOnlineDTO = JSON.parseObject(imOnlineDTOStr, IMOnlineDTO.class);
        livingRoomService.userOnlineHandler(imOnlineDTO);
    }
}
