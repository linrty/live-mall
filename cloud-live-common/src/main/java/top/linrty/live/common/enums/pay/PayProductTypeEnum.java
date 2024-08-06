package top.linrty.live.common.enums.pay;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 1:22
 * @Version: 1.0
 **/
@Getter
@AllArgsConstructor
public enum PayProductTypeEnum {
    LIVE_COIN(0, "直播间充值-虚拟币产品");

    private final int code;

    private final String desc;
}
