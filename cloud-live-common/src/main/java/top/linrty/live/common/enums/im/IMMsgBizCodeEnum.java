package top.linrty.live.common.enums.im;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/6 17:25
 * @Version: 1.0
 **/
@AllArgsConstructor
@Getter
public enum IMMsgBizCodeEnum {

    LIVING_ROOM_IM_CHAT_MSG_BIZ(1001, "直播间im聊天消息"),

    LIVING_ROOM_SEND_GIFT_SUCCESS(1002, "送礼成功"),

    LIVING_ROOM_SEND_GIFT_FAIL(1003, "送礼失败"),

    LIVING_ROOM_PK_SEND_GIFT_SUCCESS(1004, "PK送礼成功"),

    LIVING_ROOM_PK_ONLINE(1005, "PK连线"),

    RED_PACKET_CONFIG(1006, "开启红包雨活动");

    private final int code;

    private final String desc;
}
