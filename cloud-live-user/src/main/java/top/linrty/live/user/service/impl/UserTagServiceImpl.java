package top.linrty.live.user.service.impl;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.linrty.live.common.constants.UserTagFieldNameConstants;
import top.linrty.live.common.enums.UserTagsEnum;
import top.linrty.live.user.domain.po.UserTag;
import top.linrty.live.user.mapper.IUserTagMapper;
import top.linrty.live.user.service.IUserTagService;
import top.linrty.live.user.utils.TagInfoUtils;
import top.linrty.live.user.utils.UserProviderCacheKeyBuilder;

import javax.annotation.Resource;
import java.time.Duration;

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

    @Resource
    private UserProviderCacheKeyBuilder userProviderCacheKeyBuilder;

    @Override
    public boolean setTag(Long userId, UserTagsEnum userTagsEnum) {
        boolean updateStatus = baseMapper.setTag(userId, userTagsEnum.getFieldName(), userTagsEnum.getTag()) > 0;
        if (updateStatus) {//为true说明是有记录且是第一次设置（我们的sql语句是当前没有设置该tag才进行设置，即第一次设置）
            return true;
        }
        //没成功：说明是没此行记录，或者重复设置
        UserTag userTagPo = baseMapper.selectById(userId);
        if(userTagPo != null) {//重复设置，直接返回false
            return false;
        }
        //无记录，插入
        //TODO 使用Redis的setnx命令构建分布式锁（目前有很多缺陷）
        String lockKey = userProviderCacheKeyBuilder.buildTagLockKey(userId);
        try {
            Boolean isLock = redisTemplate.opsForValue().setIfAbsent(lockKey, "-1", Duration.ofSeconds(3L));
            if(BooleanUtil.isFalse(isLock)) {//说明有其他线程正在进行插入
                return false;
            }
            userTagPo = new UserTag();
            userTagPo.setUserId(userId);
            baseMapper.insert(userTagPo);
        } finally {
            redisTemplate.delete(lockKey);
        }
        System.out.println("设置标签册成功！");
        //插入后再修改返回
        return baseMapper.setTag(userId, userTagsEnum.getFieldName(), userTagsEnum.getTag()) > 0;
    }

    @Override
    public boolean cancelTag(Long userId, UserTagsEnum userTagsEnum) {
        return baseMapper.cancelTag(userId, userTagsEnum.getFieldName(), userTagsEnum.getTag()) > 0;
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
}
