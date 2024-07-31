package top.linrty.live.common.enums;

import lombok.Getter;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/30 23:06
 * @Version: 1.0
 **/
@Getter
public enum IMMsgCodeEnum {

    IM_LOGIN_MSG(1001, "登录im消息包"),

    IM_LOGOUT_MSG(1002, "登出im消息包"),

    IM_BIZ_MSG(1003, "常规业务消息包"),

    IM_HEARTBEAT_MSG(1004, "im服务心跳消息包"),
    ;

    private final int code;

    private final String desc;
    IMMsgCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
