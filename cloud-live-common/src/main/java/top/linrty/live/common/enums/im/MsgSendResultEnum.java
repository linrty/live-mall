package top.linrty.live.common.enums.im;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: IM消息发送结果枚举
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/28 20:59
 * @Version: 1.0
 **/
@Getter
@AllArgsConstructor
public enum MsgSendResultEnum {

    SEND_SUCCESS(1000,"成功"),

    SEND_FAIL(1001,"发送失败"),

    MSG_PARAM_ERROR(1002,"消息格式异常");

    private final int code;

    private final String desc;

}
