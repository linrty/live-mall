package top.linrty.live.common.config.auth;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/28 23:11
 * @Version: 1.0
 **/
@Component
@Data
public class AuthConfig {

    @Value("${auth.enabled}")
    private boolean enabled;

    @Value("${auth.include-paths}")
    private List<String> includePaths;

    @Value("${auth.exclude-paths}")
    private List<String> excludePaths;
}
