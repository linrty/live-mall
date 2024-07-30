package top.linrty.live.common.config.auth;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
@ConfigurationProperties(prefix = "auth")
public class AuthConfig {

    private boolean enabled;

    private List<String> includePaths;

    private List<String> excludePaths;
}
