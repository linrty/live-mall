package top.linrty.live.living.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.linrty.live.common.config.redis.RedisKeyTime;
import top.linrty.live.common.domain.dto.living.GiftConfigDTO;
import top.linrty.live.common.domain.vo.living.GiftConfigVO;
import top.linrty.live.common.enums.StatusEnum;
import top.linrty.live.common.exception.UnknownException;
import top.linrty.live.common.utils.ConvertBeanUtils;
import top.linrty.live.living.domain.po.GiftConfig;
import top.linrty.live.living.mapper.GiftConfigMapper;
import top.linrty.live.living.service.IGiftConfigService;
import top.linrty.live.living.utils.GiftProviderCacheKeyBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/6 22:01
 * @Version: 1.0
 **/
@Service
@Slf4j
public class GiftConfigServiceImpl implements IGiftConfigService {

    @Resource
    private GiftConfigMapper giftConfigMapper;

    @Resource
    private GiftProviderCacheKeyBuilder giftProviderCacheKeyBuilder;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    @Override
    public GiftConfigVO getByGiftId(Integer giftId) {
        LambdaQueryWrapper<GiftConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GiftConfig::getGiftId, giftId);
        queryWrapper.eq(GiftConfig::getStatus, StatusEnum.VALID_STATUS.getCode());
        queryWrapper.last("limit 1");
        GiftConfig giftConfigPO = giftConfigMapper.selectOne(queryWrapper);
        return ConvertBeanUtils.convert(giftConfigPO, GiftConfigVO.class);
    }

    @Override
    public List<GiftConfigVO> queryGiftList() {
        String cacheKey = giftProviderCacheKeyBuilder.buildGiftListCacheKey();
        List<GiftConfigVO> giftConfigVOS = redisTemplate.opsForList().range(cacheKey, 0, -1).stream().map(x -> (GiftConfigVO) x).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(giftConfigVOS)) {
            if (giftConfigVOS.get(0).getGiftId() == null) {
                return Collections.emptyList();
            }
            return giftConfigVOS;
        }
        LambdaQueryWrapper<GiftConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GiftConfig::getStatus, StatusEnum.VALID_STATUS.getCode());
        List<GiftConfig> giftConfigPOList = giftConfigMapper.selectList(queryWrapper);
        giftConfigVOS = ConvertBeanUtils.convertList(giftConfigPOList, GiftConfigVO.class);
        if (CollectionUtils.isEmpty(giftConfigVOS)) {
            redisTemplate.opsForList().leftPush(cacheKey, new GiftConfigVO());
            redisTemplate.expire(cacheKey, RedisKeyTime.EXPIRE_TIME_ONE_MINUTE, TimeUnit.SECONDS);
            return Collections.emptyList();
        }
        // 往Redis初始化List时，要上锁，避免重复写入造成数据重复
        RLock lock = redissonClient.getLock(giftProviderCacheKeyBuilder.buildGiftListLockCacheKey());
        try{
            Boolean isLock = lock.tryLock(1, 3L, TimeUnit.SECONDS);
            if (BooleanUtil.isFalse(isLock)){
                throw new UnknownException("获取列表失败");
            }
            redisTemplate.opsForList().leftPushAll(cacheKey, giftConfigVOS.toArray());
            redisTemplate.expire(cacheKey, 30L, TimeUnit.MINUTES);
        }catch (Exception e){
            log.error("获取锁失败");
            throw new UnknownException("获取列表失败");
        } finally {
            lock.unlock();
        }
        return giftConfigVOS;
    }

    @Override
    public void insertOne(GiftConfigDTO giftConfigDTO) {
        GiftConfig giftConfig = ConvertBeanUtils.convert(giftConfigDTO, GiftConfig.class);
        giftConfig.setStatus(StatusEnum.VALID_STATUS.getCode());
        giftConfigMapper.insert(giftConfig);
    }

    @Override
    public void updateOne(GiftConfigDTO giftConfigDTO) {
        GiftConfig giftConfig = ConvertBeanUtils.convert(giftConfigDTO, GiftConfig.class);
        giftConfigMapper.updateById(giftConfig);
    }
}
