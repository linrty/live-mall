package top.linrty.live.common.config.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import top.linrty.live.common.domain.vo.CommonRespVO;
import top.linrty.live.common.enums.ResultCodeEnum;
import top.linrty.live.common.exception.NotLoginException;

/**
 * @Description: 统一异常处理的核心，是一种作用于控制层的切面通知（Advice），
 *               该注解能够将通用的@ExceptionHandler、@InitBinder和@ModelAttributes方法收集到一个类型，
 *               并应用到所有控制器上
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/21 0:27
 * @Version: 1.0
 **/
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandlerAdvice {

    /**-------- 通用异常处理方法 --------**/
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public <T> CommonRespVO<T> error(Exception e) {
        log.error("全局异常捕获：" + e);
        return CommonRespVO.fail();    // 通用异常结果
    }

    /**--------未登录--------**/
    @ExceptionHandler(NotLoginException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public <T> CommonRespVO<T> error(NotLoginException e) {
        log.error("全局异常捕获：" + e);
        return CommonRespVO.setResult(ResultCodeEnum.NOT_LOGIN_ERROR);
    }

    /**--------方法参数校验--------**/
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonRespVO handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        return CommonRespVO.setResult(ResultCodeEnum.ERROR_PARAM).setMsg(e.getBindingResult().getFieldError().getDefaultMessage());
    }
}
