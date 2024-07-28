package top.linrty.live.common.utils;

import java.util.concurrent.*;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/28 21:04
 * @Version: 1.0
 **/
public class ThreadPoolManager {
    public static ThreadPoolExecutor commonAsyncPool = new ThreadPoolExecutor(2, 8, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000)
            , new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread newThread = new Thread(r);
            newThread.setName(" commonAsyncPool - " + ThreadLocalRandom.current().nextInt(10000));
            return newThread;
        }
    });
}
