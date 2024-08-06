package top.linrty.live.common.enums.living;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/6 15:18
 * @Version: 1.0
 **/
@AllArgsConstructor
@Getter
public enum LivingRoomTypeEnum {

    DEFAULT_LIVING_ROOM(1, "默认直播间"),

    PK_LIVING_ROOM(2, "PK直播间"),
    ;

    private final Integer code;

    private final String desc;
}
