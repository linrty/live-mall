package top.linrty.live.common.config.redis;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/30 22:12
 * @Version: 1.0
 **/
public class RedisKeyTime {

    // 1年
    public static final long EXPIRE_TIME_ONE_YEAR = 60 * 60 * 24 * 365;
    // 30天
    public static final long EXPIRE_TIME_ONE_MONTH = 60 * 60 * 24 * 30;
    // 7天
    public static final long EXPIRE_TIME_ONE_WEEK = 60 * 60 * 24 * 7;

    // 1天
    public static final long EXPIRE_TIME_ONE_DAY = 60 * 60 * 24;

    // 1小时
    public static final long EXPIRE_TIME_ONE_HOUR = 60 * 60 * 12;

    // 30分钟
    public static final long EXPIRE_TIME_HALF_HOUR = 60 * 30;

    // 15分钟
    public static final long EXPIRE_TIME_HALF_HOUR_15 = 60 * 15;

    // 1分钟
    public static final long EXPIRE_TIME_ONE_MINUTE = 60;


}
