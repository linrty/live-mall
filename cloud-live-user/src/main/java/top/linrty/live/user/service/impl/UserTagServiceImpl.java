package top.linrty.live.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.val;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import top.linrty.live.common.constants.UserTagFieldNameConstants;
import top.linrty.live.common.domain.dto.UserTagDTO;
import top.linrty.live.common.domain.po.KafkaObject;
import top.linrty.live.common.enums.KafkaCodeEnum;
import top.linrty.live.common.enums.UserTagsEnum;
import top.linrty.live.user.domain.po.UserTag;
import top.linrty.live.user.mapper.IUserTagMapper;
import top.linrty.live.user.service.IUserTagService;
import top.linrty.live.user.utils.TagInfoUtils;
import top.linrty.live.user.utils.UserProviderCacheKeyBuilder;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/21 23:20
 * @Version: 1.0
 **/
@Service
public class UserTagServiceImpl extends ServiceImpl<IUserTagMapper, UserTag> implements IUserTagService {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, UserTagDTO> userTagDTORedisTemplate;

    @Resource
    private UserProviderCacheKeyBuilder userProviderCacheKeyBuilder;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public boolean setTag(Long userId, UserTagsEnum userTagsEnum) {
        boolean updateStatus = baseMapper.setTag(userId, userTagsEnum.getFieldName(), userTagsEnum.getTag()) > 0;
        if (updateStatus) {//为true说明是有记录且是第一次设置（我们的sql语句是当前没有设置该tag才进行设置，即第一次设置）
            //更改操作，删除缓存
            userTagDTORedisTemplate.delete(userProviderCacheKeyBuilder.buildTagInfoKey(userId));
            //TODO 计划更改为canal实现延迟双删或双写
            KafkaObject kafkaObject = new KafkaObject(KafkaCodeEnum.USER_TAG_INFO.getCode(), userId.toString());
            kafkaTemplate.send("user-delete-cache", JSONUtil.toJsonStr(kafkaObject));
            return true;
        }
        //没成功：说明是没此行记录，或者重复设置
        UserTag userTagPo = baseMapper.selectById(userId);
        if(userTagPo != null) {//重复设置，直接返回false
            return false;
        }
        //无记录，插入
        String lockKey = userProviderCacheKeyBuilder.buildTagLockKey(userId);
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean res = lock.tryLock(3L, 60L, TimeUnit.SECONDS);
            if (BooleanUtil.isFalse(res)) {
                return false;
            }
            userTagPo = new UserTag();
            userTagPo.setUserId(userId);
            baseMapper.insert(userTagPo);
        } catch (Exception e) {
            log.error("获取锁失败，失败原因：" + e.getMessage());
        } finally {
            //无论如何, 最后都要解锁
            lock.unlock();
        }
        //插入后再修改返回
        return baseMapper.setTag(userId, userTagsEnum.getFieldName(), userTagsEnum.getTag()) > 0;
    }

    @Override
    public boolean cancelTag(Long userId, UserTagsEnum userTagsEnum) {
        boolean cancelStatus = baseMapper.cancelTag(userId, userTagsEnum.getFieldName(), userTagsEnum.getTag()) > 0;
        if(!cancelStatus) {
            return false;
        }
        //更改操作，删除缓存
        userTagDTORedisTemplate.delete(userProviderCacheKeyBuilder.buildTagInfoKey(userId));
        //TODO 计划更改为canal实现延迟双删或双写
        KafkaObject kafkaObject = new KafkaObject(KafkaCodeEnum.USER_TAG_INFO.getCode(), userId.toString());
        kafkaTemplate.send("user-delete-cache", JSONUtil.toJsonStr(kafkaObject));
        return true;
    }

    @Override
    public boolean containTag(Long userId, UserTagsEnum userTagsEnum) {
        UserTag userTag = baseMapper.selectById(userId);
        if (userTag == null) {
            return false;
        }
        String fieldName = userTagsEnum.getFieldName();
        if (fieldName.equals(UserTagFieldNameConstants.TAT_INFO_01)) {
            return TagInfoUtils.isContain(userTag.getTagInfo01(), userTagsEnum.getTag());
        } else if (fieldName.equals(UserTagFieldNameConstants.TAT_INFO_02)) {
            return TagInfoUtils.isContain(userTag.getTagInfo02(), userTagsEnum.getTag());
        } else if (fieldName.equals(UserTagFieldNameConstants.TAT_INFO_03)) {
            return TagInfoUtils.isContain(userTag.getTagInfo03(), userTagsEnum.getTag());
        }
        return false;
    }

    /**
     * 从Redis中查询缓存的用户标签
     * @param userId
     * @return
     */
    private UserTagDTO queryTagInfoFromRedisCache(Long userId) {
        String key = userProviderCacheKeyBuilder.buildTagInfoKey(userId);
        UserTagDTO userTagDTO = userTagDTORedisTemplate.opsForValue().get(key);
        if(userTagDTO != null) {
            return userTagDTO;
        }
        UserTag userTag = baseMapper.selectById(userId);
        if(userTag == null) {
            return null;
        }
        userTagDTO = BeanUtil.copyProperties(userTag, UserTagDTO.class);
        userTagDTORedisTemplate.opsForValue().set(key, userTagDTO);
        return userTagDTO;
    }
}
