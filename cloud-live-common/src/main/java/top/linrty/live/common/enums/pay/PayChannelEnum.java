package top.linrty.live.common.enums.pay;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 2:05
 * @Version: 1.0
 **/
@Getter
@AllArgsConstructor
public enum PayChannelEnum {

    ZHI_FU_BAO(0, "支付宝"),

    WEI_XIN(1, "微信"),

    YIN_LIAN(2, "银联"),

    SHOU_YIN_TAI(3, "收银台");

    private final int code;

    private final String desc;
}
