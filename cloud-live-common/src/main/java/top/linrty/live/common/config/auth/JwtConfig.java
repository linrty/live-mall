package top.linrty.live.common.config.auth;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/28 22:44
 * @Version: 1.0
 **/
@Data
@Component
public class JwtConfig {

    @Value("${auth.jwt.secret}")
    private String secret;

    @Value("${auth.jwt.header-alg}")
    private String headerAlg;

    @Value("${auth.jwt.iss}")
    private String iss;

    @Value("${auth.jwt.exp}")
    private String expireTime;

    @Value("${auth.jwt.aud}")
    private String aud;
}
