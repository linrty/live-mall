package top.linrty.live.im.utils;

import io.netty.channel.ChannelHandlerContext;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/1 15:20
 * @Version: 1.0
 **/
public class IMContext {
    public static Long getUserId(ChannelHandlerContext ctx) {
        return ctx.attr(IMContextAttr.USER_ID).get();
    }

    public static void setUserId(ChannelHandlerContext ctx, Long userId) {
        ctx.attr(IMContextAttr.USER_ID).set(userId);
    }

    public static void removeUserId(ChannelHandlerContext ctx) {
        ctx.attr(IMContextAttr.USER_ID).remove();
    }

    public static Integer getAppId(ChannelHandlerContext ctx) {
        return ctx.attr(IMContextAttr.APP_ID).get();
    }

    public static void setAppId(ChannelHandlerContext ctx, Integer appId) {
        ctx.attr(IMContextAttr.APP_ID).set(appId);
    }

    public static void removeAppId(ChannelHandlerContext ctx) {
        ctx.attr(IMContextAttr.APP_ID).remove();
    }

    public static Integer getRoomId(ChannelHandlerContext ctx) {
        return ctx.attr(IMContextAttr.ROOM_ID).get();
    }

    public static void setRoomId(ChannelHandlerContext ctx, Integer roomId) {
        ctx.attr(IMContextAttr.ROOM_ID).set(roomId);
    }

    public static void removeRoomId(ChannelHandlerContext ctx) {
        ctx.attr(IMContextAttr.ROOM_ID).remove();
    }
}
