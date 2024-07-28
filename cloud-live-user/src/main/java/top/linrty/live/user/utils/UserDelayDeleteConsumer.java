package top.linrty.live.user.utils;

import cn.hutool.json.JSONUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import top.linrty.live.common.domain.po.KafkaObject;
import top.linrty.live.common.enums.KafkaCodeEnum;
import top.linrty.live.common.utils.DelayedTask;

import jakarta.annotation.Resource;
import java.util.concurrent.*;

/**
 * @Description: 用户缓存延迟双删
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/20 23:42
 * @Version: 1.0
 **/
@Component
@Slf4j
public class UserDelayDeleteConsumer {
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private UserProviderCacheKeyBuilder userProviderCacheKeyBuilder;

    private static final DelayQueue<DelayedTask> DELAY_QUEUE = new DelayQueue<>();

    private static final ExecutorService DELAY_QUEUE_THREAD_POOL = new ThreadPoolExecutor(
            3, 10,
            10L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(100)
    );

    @PostConstruct()
    private void init() {
        DELAY_QUEUE_THREAD_POOL.submit(() -> {
            while (true) {
                try {
                    DelayedTask task = DELAY_QUEUE.take();
                    task.execute();
                    log.info("DelayQueue延迟双删了一个用户缓存");
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                    e.printStackTrace();
                }
            }
        }, "Thread-user-delay-delete-cache");
    }

    @KafkaListener(topics = "user-delete-cache")
    public void consumerTopic(String kafkaObjectJSON) {
        KafkaObject kafkaObject = JSONUtil.toBean(kafkaObjectJSON, KafkaObject.class);
        String code = kafkaObject.getCode();
        log.info("Kafka接收到的json：{}", kafkaObjectJSON);
        long userId = Long.parseLong(kafkaObject.getUserId());
        if(code.equals(KafkaCodeEnum.USER_INFO.getCode())) {
            DELAY_QUEUE.offer(new DelayedTask(1000,
                    () -> redisTemplate.delete(userProviderCacheKeyBuilder.buildUserInfoKey(userId))));
            log.info("Kafka接收延迟双删消息成功，类别：UserInfo，用户ID：{}", userId);
        }else if (code.equals(KafkaCodeEnum.USER_TAG_INFO.getCode())){
            DELAY_QUEUE.offer(new DelayedTask(1000,
                    () -> redisTemplate.delete(userProviderCacheKeyBuilder.buildTagInfoKey(userId))));
            log.info("Kafka接收延迟双删消息成功，类别：UserTagInfo，用户ID：{}", userId);
        }
    }

}
