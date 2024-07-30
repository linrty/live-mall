package top.linrty.live.user.service.impl;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import top.linrty.live.api.clients.UserClient;
import top.linrty.live.common.domain.dto.user.MsgCheckDTO;
import top.linrty.live.common.domain.dto.user.UserDTO;
import top.linrty.live.common.enums.MsgSendResultEnum;
import top.linrty.live.common.enums.UserTagsEnum;
import top.linrty.live.user.service.ISmsService;
import top.linrty.live.user.service.IUserService;
import top.linrty.live.user.service.IUserTagService;


/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/20 17:43
 * @Version: 1.0
 **/
@DubboService(version = "1.0.0")
public class UserClientImpl implements UserClient {

    @Resource
    private IUserService userService;

    @Resource
    private IUserTagService userTagService;

    @Resource
    private ISmsService smsService;



    @Override
    public UserDTO getUserById(Long userId) {
        return userService.getUserById(userId);
    }

    @Override
    public boolean updateUserInfo(UserDTO userDTO) {
        return userService.updateUserInfo(userDTO);
    }

    @Override
    public boolean insertOne(UserDTO userDTO) {
        return userService.insertOne(userDTO);
    }

    @Override
    public boolean setTag(Long userId, UserTagsEnum userTagsEnum) {
        return userTagService.setTag(userId, userTagsEnum);
    }

    @Override
    public boolean cancelTag(Long userId, UserTagsEnum userTagsEnum) {
        return userTagService.cancelTag(userId, userTagsEnum);
    }

    @Override
    public boolean containTag(Long userId, UserTagsEnum userTagsEnum) {
        return userTagService.containTag(userId, userTagsEnum);
    }

    @Override
    public MsgSendResultEnum sendLoginCode(String phone) {
        return smsService.sendLoginCode(phone);
    }

    @Override
    public MsgCheckDTO checkLoginCode(String phone, Integer code) {
        return smsService.checkLoginCode(phone, code);
    }
}
