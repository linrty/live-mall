package top.linrty.live.living.utils;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import top.linrty.live.common.constants.im.IMMsgTopicNames;
import top.linrty.live.common.domain.dto.im.IMOfflineDTO;
import top.linrty.live.living.service.ILivingRoomService;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/6 17:12
 * @Version: 1.0
 **/
@Component
public class LivingRoomOfflineConsumer {

    @Resource
    private ILivingRoomService livingRoomService;

    @KafkaListener(topics = IMMsgTopicNames.IM_OFFLINE_TOPIC, groupId = "im-offline-consumer")
    public void consumeOnline(String imOfflineDTOStr) {
        IMOfflineDTO imOfflineDTO = JSON.parseObject(imOfflineDTOStr, IMOfflineDTO.class);
        livingRoomService.userOfflineHandler(imOfflineDTO);
    }
}
