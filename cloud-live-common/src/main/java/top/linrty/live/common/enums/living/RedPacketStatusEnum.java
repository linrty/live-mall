package top.linrty.live.common.enums.living;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 15:37
 * @Version: 1.0
 **/
@Getter
@AllArgsConstructor
public enum RedPacketStatusEnum {
    WAIT(1,"待准备"),

    IS_PREPARED(2, "已准备"),

    IS_SEND(3, "已发送");

    private final int code;

    private final String desc;
}
