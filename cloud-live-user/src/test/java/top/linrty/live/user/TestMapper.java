package top.linrty.live.user;

import com.baomidou.dynamic.datasource.annotation.DS;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.linrty.live.common.domain.dto.user.MsgCheckDTO;
import top.linrty.live.common.enums.MsgSendResultEnum;
import top.linrty.live.user.domain.po.User;
import top.linrty.live.user.mapper.IUserMapper;
import top.linrty.live.user.service.ISmsService;
import top.linrty.live.user.service.IUserService;

import java.util.Date;
import java.util.Scanner;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/26 0:08
 * @Version: 1.0
 **/
@SpringBootTest
public class TestMapper {

    @Resource
    private IUserService userService;

    @Resource
    private ISmsService smsService;

    @Test
    public void testUserTable(){
        User user = new User();
        user.setAvatar("https://avatars.githubusercontent.com/u/10162109?v=4")
                .setSex(1)
                .setBornDate(new Date())
                .setBornCity(5)
                .setWorkCity(5)
                .setTrueName("linrty")
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
        userService.save(user);
    }

    @Test
    public void testSendCode(){
        String phone = "17679374162";
        MsgSendResultEnum msgSendResultEnum = smsService.sendLoginCode(phone);
        System.out.println(msgSendResultEnum);
        while (true) {
            System.out.println("输入验证码：");
            Scanner scanner = new Scanner(System.in);
            int code = scanner.nextInt();
            MsgCheckDTO msgCheckDTO = smsService.checkLoginCode(phone, code);
            System.out.println(msgCheckDTO);
        }
    }
}
