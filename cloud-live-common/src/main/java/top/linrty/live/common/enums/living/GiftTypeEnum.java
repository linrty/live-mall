package top.linrty.live.common.enums.living;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/6 19:28
 * @Version: 1.0
 **/
@AllArgsConstructor
@Getter
public enum GiftTypeEnum {

    DEFAULT_SEND_GIFT(0, "直播间默认送礼物"),

    PK_SEND_GIFT(1, "直播间PK送礼物");


    private final Integer code;

    private final String desc;
}
