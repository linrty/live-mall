package top.linrty.live.common.enums;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/20 23:35
 * @Version: 1.0
 **/
public enum KafkaCodeEnum {

    USER_INFO("0001", "USER_INFO"),
    USER_TAG_INFO("0002", "USER_TAG_INFO"),
    ;

    private String code;

    private String message;

    private KafkaCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
}
