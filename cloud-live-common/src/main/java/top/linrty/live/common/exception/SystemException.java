package top.linrty.live.common.exception;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/30 17:25
 * @Version: 1.0
 **/
public class SystemException extends RuntimeException{
    public SystemException() {
        super("请求异常");
    }

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemException(Throwable cause) {
        super("请求异常",cause);
    }
}
