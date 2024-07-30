package top.linrty.live.common.exception;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/30 18:05
 * @Version: 1.0
 **/
public class UnknownException extends RuntimeException{
    public UnknownException() {
        super("请求异常");
    }

    public UnknownException(String message) {
        super(message);
    }

    public UnknownException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownException(Throwable cause) {
        super("请求异常",cause);
    }
}
