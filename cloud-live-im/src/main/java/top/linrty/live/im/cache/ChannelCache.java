package top.linrty.live.im.cache;

import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/1 16:02
 * @Version: 1.0
 **/
public class ChannelCache {

    private static String serverIp = "";
    private static Map<Long, ChannelHandlerContext> channelHandlerContextMap = new HashMap<>();

    public static ChannelHandlerContext get(Long userId) {
        return channelHandlerContextMap.get(userId);
    }

    public static void put(Long userId, ChannelHandlerContext channelHandlerContext) {
        channelHandlerContextMap.put(userId, channelHandlerContext);
    }

    public static void remove(Long userId) {
        channelHandlerContextMap.remove(userId);
    }

    public static void setServerIp(String serverIp) {
        ChannelCache.serverIp = serverIp;
    }

    public static String getServerIp() {
        return serverIp;
    }
}
