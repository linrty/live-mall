package top.linrty.live.common.exception;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/21 0:34
 * @Version: 1.0
 **/
public class NotLoginException extends RuntimeException{
    public NotLoginException() {
        super("未登录");
    }

    public NotLoginException(String message) {
        super(message);
    }

    public NotLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotLoginException(Throwable cause) {
        super("未登录",cause);
    }
}
