package top.linrty.live.user;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import top.linrty.live.common.config.redis.RedisKeyTime;
import top.linrty.live.common.domain.dto.user.MsgCheckDTO;
import top.linrty.live.common.enums.im.MsgSendResultEnum;
import top.linrty.live.user.domain.po.User;
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
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testUserTable(){
        redisTemplate.opsForValue().set("test", null);
        if (redisTemplate.opsForValue().get("test") == null){
            System.out.println("null");
        }
    }

}
