package top.linrty.live.shop.utils;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 延迟任务
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 22:15
 * @Version: 1.0
 **/
@Accessors(chain = true)
@Data
public class DelayedTask implements Delayed {

    /**
     * 任务到期时间
     */
    private long executeTime;
    /**
     * 任务
     */
    private Runnable task;

    /**
     * 查看当前任务还有多久到期
     * @param unit the time unit
     * @return
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(executeTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * 延迟队列需要到期时间升序入队，所以我们需要实现compareTo进行到期时间比较
     * @param o the object to be compared.
     * @return
     */
    @Override
    public int compareTo(Delayed o) {
        return Long.compare(this.executeTime, ((DelayedTask) o).executeTime);
    }

    public void execute() {
        task.run();
    }
}
