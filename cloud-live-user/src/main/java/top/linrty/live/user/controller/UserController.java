package top.linrty.live.user.controller;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import top.linrty.live.common.domain.dto.user.UserDTO;
import top.linrty.live.user.service.IUserService;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/20 20:25
 * @Version: 1.0
 **/
@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @GetMapping("/getUserInfo")
    public UserDTO getUserInfo(Long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/updateUserInfo")
    public boolean updateUserInfo(UserDTO userDTO) {
        return userService.updateUserInfo(userDTO);
    }

    @GetMapping("/insertUserInfo")
    public boolean insertUserInfo(UserDTO userDTO) {
        return userService.insertOne(userDTO);
    }

    @GetMapping("/batchQueryUserInfo")
    public Map<Long, UserDTO> batchQueryUserInfo(String userIdStr) {
        return userService.batchQueryUserInfo(Arrays.stream(userIdStr.split(","))
                .map(Long::valueOf).collect(Collectors.toList()));
    }
}
