package top.linrty.live.common.enums.pay;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 2:03
 * @Version: 1.0
 **/
@Getter
@AllArgsConstructor
public enum PaySourceEnum {
    PAY_LIVING_ROOM(1, "直播间内支付"),

    PAY_USER_CENTER(2, "用户中心内支付");

    private final int code;

    private final String desc;

    public static PaySourceEnum find(int code) {
        for (PaySourceEnum value : PaySourceEnum.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }
}
