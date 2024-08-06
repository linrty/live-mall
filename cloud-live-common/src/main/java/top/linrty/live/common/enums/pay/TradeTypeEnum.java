package top.linrty.live.common.enums.pay;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 1:31
 * @Version: 1.0
 **/
@Getter
@AllArgsConstructor
public enum TradeTypeEnum {
    SEND_GIFT_TRADE(0, "送礼物交易"),
    LIVING_RECHARGE(1, "直播间充值");

    private final Integer code;
    private final String desc;
}
