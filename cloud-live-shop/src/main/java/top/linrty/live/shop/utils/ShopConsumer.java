package top.linrty.live.shop.utils;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import top.linrty.live.common.constants.living.GiftTopicNames;
import top.linrty.live.common.domain.dto.shop.RollBackStockDTO;
import top.linrty.live.shop.service.ISkuStockInfoService;

import java.util.concurrent.*;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 22:17
 * @Version: 1.0
 **/
@Component
@Slf4j
public class ShopConsumer {

    @Resource
    private ISkuStockInfoService skuStockInfoService;


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
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @KafkaListener(topics = GiftTopicNames.ROLL_BACK_STOCK, groupId = "stock-roll-back")
    public void stockRollBack(String rollBackStockBoStr) {
        RollBackStockDTO rollBackStockDTO = JSON.parseObject(rollBackStockBoStr, RollBackStockDTO.class);
        DELAY_QUEUE.offer(
                new DelayedTask()
                        .setExecuteTime(30* 60 * 1000L)
                        .setTask(() -> skuStockInfoService.stockRollBackHandler(rollBackStockDTO))
        );
        log.info("[StockRollBackConsumer] rollback success, rollbackInfo is {}", rollBackStockDTO);
    }

    @KafkaListener(topics = GiftTopicNames.START_LIVING_ROOM, groupId = "start-living-room-consumer")
    public void startLivingRoom(String anchorIdStr) {
        Long anchorId = Long.valueOf(anchorIdStr);
        boolean isSuccess = skuStockInfoService.prepareStockInfo(anchorId);
        if (isSuccess) {
            log.info("[StartLivingRoomConsumer] 同步库存到Redis成功，主播id：{}", anchorId);
        }
    }


}
