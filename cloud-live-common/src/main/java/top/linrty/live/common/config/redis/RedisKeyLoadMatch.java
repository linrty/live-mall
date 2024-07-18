package top.linrty.live.common.config.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 让各类keyBuilder能够根据类名与引入的模块的spring.application.name做匹配，进行条件装配
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/19 0:17
 * @Version: 1.0
 **/
@Slf4j
public class RedisKeyLoadMatch implements Condition {

    private static final String PREFIX = "live-mall";


    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String appName = context.getEnvironment().getProperty("spring.application.name");
        if (appName == null) {
            log.error("没有匹配到应用名称，所以无法加载任何RedisKeyBuilder对象");
            return false;
        }
        try {
            Field classNameField = metadata.getClass().getDeclaredField("className");
            classNameField.setAccessible(true);
            String keyBuilderName = (String) classNameField.get(metadata);
            List<String> splitList = Arrays.asList(keyBuilderName.split("\\."));
            //忽略大小写，统一用live-mall开头命名
            String classSimplyName = PREFIX + splitList.get(splitList.size() - 1).toLowerCase();
            boolean matchStatus = classSimplyName.contains(appName.replaceAll("-", ""));
            log.info("keyBuilderClass is {},matchStatus is {}", keyBuilderName, matchStatus);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
