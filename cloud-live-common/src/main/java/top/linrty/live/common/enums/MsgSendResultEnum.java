package top.linrty.live.common.enums;

import lombok.Getter;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/28 20:59
 * @Version: 1.0
 **/
@Getter
public enum MsgSendResultEnum {
    SEND_SUCCESS(0,"成功"),

    SEND_FAIL(1,"发送失败"),

    MSG_PARAM_ERROR(2,"消息格式异常");

    private int code;
    private String desc;

    MsgSendResultEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
