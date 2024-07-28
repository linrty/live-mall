package top.linrty.live.common.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/19 0:10
 * @Version: 1.0
 **/

@Configuration
@ConditionalOnClass(RedisTemplate.class)
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class RedisConfig {

    // 从配置文件中读取Redis主机信息
    @Value("${spring.redis.host}")
    private String redisHost;

    // 从配置文件中读取Redis端口信息
    @Value("${spring.redis.port}")
    private int redisPort;

    // password
    @Value("${spring.redis.password}")
    private String redisPassword;

    // 配置Redis连接工厂
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // 创建Redis的单机配置
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        config.setPassword(redisPassword);
        // 返回Lettuce连接工厂
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        IGenericJackson2JsonRedisSerializer valueSerializer = new IGenericJackson2JsonRedisSerializer();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(valueSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
