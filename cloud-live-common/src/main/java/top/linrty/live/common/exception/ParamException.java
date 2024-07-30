package top.linrty.live.common.exception;

/**
 * @Description: 自定义参数异常
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/30 17:01
 * @Version: 1.0
 **/
public class ParamException extends RuntimeException{
    public ParamException() {
        super("参数错误");
    }

    public ParamException(String message) {
        super(message);
    }

    public ParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamException(Throwable cause) {
        super("参数错误",cause);
    }
}
