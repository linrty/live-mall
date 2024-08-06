package top.linrty.live.common.enums.pay;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 2:06
 * @Version: 1.0
 **/
@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    WAITING_PAY(0, "待支付"),

    PAYING(1, "支付中"),

    PAYED(2, "已支付"),

    PAY_BACK(3, "撤销"),

    IN_VALID(4, "无效");

    private final int code;

    private final String desc;
}
