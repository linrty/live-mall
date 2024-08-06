package top.linrty.live.im.utils;

import io.netty.util.AttributeKey;

/**
 * @Description: 看作SpringBoot的RequestAttribute
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/1 15:22
 * @Version: 1.0
 **/
public class IMContextAttr {
    /**
     * 绑定用户id
     */
    public static AttributeKey<Long> USER_ID = AttributeKey.valueOf("userId");

    /**
     * 绑定appId
     */
    public static AttributeKey<Integer> APP_ID = AttributeKey.valueOf("appId");

    /**
     * roomId
     */
    public static AttributeKey<Integer> ROOM_ID = AttributeKey.valueOf("roomId");
}
