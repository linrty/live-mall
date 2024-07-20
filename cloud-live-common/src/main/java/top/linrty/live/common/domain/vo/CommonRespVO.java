package top.linrty.live.common.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.linrty.live.common.enums.ResultCodeEnum;

/**
 * @Description: 统一返回结果封装
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/21 0:13
 * @Version: 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CommonRespVO<T> {
    private int code;

    private String msg;

    private T data;

    public CommonRespVO(ResultCodeEnum resultCodeEnum) {
        this.code = resultCodeEnum.getCode();
        this.msg = resultCodeEnum.getMsg();
    }

    public static <T> CommonRespVO<T> setResult(ResultCodeEnum result) {
        return new CommonRespVO<>(result);
    }

    public static <T> CommonRespVO<T> success() {
        return new CommonRespVO<>(ResultCodeEnum.SUCCESS);
    }

    public static <T> CommonRespVO<T> fail() {
        return new CommonRespVO<>(ResultCodeEnum.UNKNOWN_ERROR);
    }

}
