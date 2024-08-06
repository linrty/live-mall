package top.linrty.live.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/6 21:38
 * @Version: 1.0
 **/
@Getter
@AllArgsConstructor
public enum ErrorAppIdEnum {

    LIVE_API_ERROR(1001,"live-api-error");

    private final int code;

    private final String msg;
}
