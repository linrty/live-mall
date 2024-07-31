package top.linrty.live.common.enums;

import lombok.Getter;

/**
 * @Description: 返回结果码枚举
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/21 0:02
 * @Version: 1.0
 **/
@Getter
public enum ResultCodeEnum {

    SUCCESS(200,"success"),

    ERROR_PARAM(400,"error-param"),

    SYS_ERROR(500,"sys-error"),

    BIZ_ERROR(501,"biz-error"),

    UNKNOWN_ERROR(999,"unknown-error"),

    NOT_LOGIN_ERROR(401, "not-login-error"),
    ;

    private ResultCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private final int code;

    private final String msg;
}
