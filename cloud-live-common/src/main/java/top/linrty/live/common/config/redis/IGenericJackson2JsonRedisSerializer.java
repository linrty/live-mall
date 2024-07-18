package top.linrty.live.common.config.redis;

import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import top.linrty.live.common.config.MapperFactory;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/19 0:15
 * @Version: 1.0
 **/
public class IGenericJackson2JsonRedisSerializer extends GenericJackson2JsonRedisSerializer {

    public IGenericJackson2JsonRedisSerializer() {
        super(MapperFactory.newInstance());
    }

    @Override
    public byte[] serialize(Object source) throws SerializationException {

        if (source != null && ((source instanceof String) || (source instanceof Character))) {
            return source.toString().getBytes();
        }
        return super.serialize(source);
    }
}