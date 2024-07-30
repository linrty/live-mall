package top.linrty.live.user.service;

import top.linrty.live.common.domain.dto.user.MsgCheckDTO;
import top.linrty.live.common.enums.MsgSendResultEnum;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/28 21:38
 * @Version: 1.0
 **/
public interface ISmsService {
    /**
     * 发送短信接口
     *
     * @param phone
     * @return
     */
    MsgSendResultEnum sendLoginCode(String phone);

    /**
     * 校验登录验证码
     *
     * @param phone
     * @param code
     * @return
     */
    MsgCheckDTO checkLoginCode(String phone, Integer code);
}
