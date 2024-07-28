package top.linrty.live.user;

import com.baomidou.dynamic.datasource.annotation.DS;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.linrty.live.user.domain.po.User;
import top.linrty.live.user.mapper.IUserMapper;
import top.linrty.live.user.service.IUserService;

import java.util.Date;

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
}
