package top.linrty.live.user.service.impl;

import org.apache.dubbo.config.annotation.DubboService;
import top.linrty.live.api.clients.UserClient;
import top.linrty.live.common.domain.dto.UserDTO;
import top.linrty.live.user.service.IUserService;

import javax.annotation.Resource;

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
}
