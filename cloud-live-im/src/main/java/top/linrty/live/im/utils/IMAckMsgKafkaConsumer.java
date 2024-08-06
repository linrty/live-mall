package top.linrty.live.im.utils;

import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import top.linrty.live.common.domain.po.im.IMMsg;
import top.linrty.live.common.domain.po.im.IMMsgBody;
import top.linrty.live.common.enums.im.IMMsgCodeEnum;
import top.linrty.live.common.utils.DelayedTask;
import top.linrty.live.im.cache.ChannelCache;
import top.linrty.live.im.service.IAckMsgService;
import top.linrty.live.im.service.IRouterHandlerService;

import java.util.concurrent.*;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/4 18:54
 * @Version: 1.0
 **/
@Component
@Slf4j
public class IMAckMsgKafkaConsumer {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private IMProviderCacheKeyBuilder imProviderCacheKeyBuilder;

    @Resource
    private IAckMsgService ackMsgService;

    @Resource
    private IRouterHandlerService routerHandlerService;

    private static final DelayQueue<DelayedTask> DELAY_QUEUE = new DelayQueue<>();

    private static final ExecutorService DELAY_QUEUE_THREAD_POOL = new ThreadPoolExecutor(
        3,10,10L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));

    @PostConstruct()
    private void init(){
        DELAY_QUEUE_THREAD_POOL.submit(() -> {
            while (true){
                try {
                    DelayedTask delayedTask = DELAY_QUEUE.take();
                    delayedTask.execute();
                } catch (InterruptedException e) {
                    log.error("延迟队列异常",e);
                }
            }
        }, "ack-msg-kafka-consumer");
    }

    public void consumeAckMsg(String imMsgBodyJson, Acknowledgment ack){
        IMMsgBody imMsgBody = JSON.parseObject(imMsgBodyJson, IMMsgBody.class);

        DELAY_QUEUE.offer(new DelayedTask(4000, () -> {
            Long userId = imMsgBody.getUserId();
            Integer appId = imMsgBody.getAppId();
            String msgId = imMsgBody.getMsgId();
            int retryCount = ackMsgService.getMsgAckTimes(msgId, userId, appId);
            log.info("收到ack消息，消息id:{},重试次数:{}", imMsgBody.getMsgId(), retryCount);
            if (retryCount < 0){
                return;
            }
            // 只支持一次重发
            if( retryCount < 2){
                // 发送消息给客户端
                routerHandlerService.sendMsgToClient(imMsgBody);
                // 再次记录未收到ack的消息，count + 1
                retryCount ++;
                ackMsgService.recordMsgAck(imMsgBody, retryCount);
                // 再次重发消息
                ackMsgService.sendDelayMsg(imMsgBody);
            } else{
                // 已经执行过一次重发，不再重试，直接删除
                ackMsgService.doMsgAck(imMsgBody);
            }
            // 手动提交
            ack.acknowledge();
            log.info("消息id:{},重试次数:{}", imMsgBody.getMsgId(), retryCount);
        }));
    }
}
