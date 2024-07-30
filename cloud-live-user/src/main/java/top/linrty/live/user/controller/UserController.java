package top.linrty.live.user.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.linrty.live.common.domain.dto.user.UserDTO;
import top.linrty.live.common.domain.vo.CommonRespVO;
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
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @GetMapping("/getUserInfo")
    @ResponseBody
    public CommonRespVO getUserInfo(Long userId) {
        return CommonRespVO.success().setData(userService.getUserById(userId));
    }

    @GetMapping("/updateUserInfo")
    @ResponseBody
    public CommonRespVO updateUserInfo(UserDTO userDTO) {
        return CommonRespVO.success().setData(userService.updateUserInfo(userDTO));
    }

    @GetMapping("/insertUserInfo")
    @ResponseBody
    public CommonRespVO insertUserInfo(UserDTO userDTO) {
        return  CommonRespVO.success().setData(userService.insertOne(userDTO));
    }

    @GetMapping("/batchQueryUserInfo")
    @ResponseBody
    public CommonRespVO batchQueryUserInfo(String userIdStr) {
        return CommonRespVO.success().setData(userService.batchQueryUserInfo(Arrays.stream(userIdStr.split(","))
                .map(Long::valueOf).collect(Collectors.toList())));
    }

    @PostMapping("/sendLoginCode")
    @ResponseBody
    public CommonRespVO sendLoginCode(String phone) {
        userService.sendLoginCode(phone);
        return CommonRespVO.success();
    }

    @PostMapping("/login")
    @ResponseBody
    public CommonRespVO login(String phone, Integer code) {
        return CommonRespVO.success().setData(userService.login(phone, code));
    }
}
