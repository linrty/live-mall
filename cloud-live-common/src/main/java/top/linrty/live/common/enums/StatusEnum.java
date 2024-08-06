package top.linrty.live.common.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/4 23:50
 * @Version: 1.0
 **/
@AllArgsConstructor
@Getter
public enum StatusEnum {

    INVALID_STATUS(0,"无效"),

    VALID_STATUS(1,"有效");

    private int code;

    private String desc;

    public void setCode(int code) {
        this.code = code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
