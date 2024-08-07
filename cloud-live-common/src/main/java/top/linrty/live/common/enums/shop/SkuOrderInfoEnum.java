package top.linrty.live.common.enums.shop;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 17:01
 * @Version: 1.0
 **/
@Getter
@AllArgsConstructor
public enum SkuOrderInfoEnum {
    PREPARE_PAY(0, "待支付状态"),

    HAS_PAY(1, "已支付状态"),

    CANCEL(2, "取消订单状态");

    private final int code;

    private final String desc;
}
