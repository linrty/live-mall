package top.linrty.live.common.enums.im;

import lombok.Getter;

/**
 * @Description: IM消息类型枚举
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/30 23:06
 * @Version: 1.0
 **/
@Getter
public enum IMMsgCodeEnum {

    WS_SHARD_MSG(1000, "首次建立ws连接消息包"),

    IM_LOGIN_MSG(1001, "登录im消息包"),

    IM_LOGOUT_MSG(1002, "登出im消息包"),

    IM_BIZ_MSG(1003, "常规业务消息包"),

    IM_HEARTBEAT_MSG(1004, "im服务心跳消息包"),

    IM_ACK_MSG(1005, "im服务ack消息包"),

    IM_LIVING_ROOM_MSG(1006, "直播间消息包"),

    ;

    private final int code;

    private final String desc;
    IMMsgCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
