package top.linrty.live.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: 业务枚举类
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/1 11:32
 * @Version: 1.0
 **/
@Getter
@AllArgsConstructor
public enum BizEnum {
    LIVE_BIZ(1001,"直播业务"),
    ;

    private final int code;

    private final String desc;
}
