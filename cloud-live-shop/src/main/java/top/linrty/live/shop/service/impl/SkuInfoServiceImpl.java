package top.linrty.live.shop.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.linrty.live.common.config.redis.RedisKeyBuilder;
import top.linrty.live.common.config.redis.RedisKeyTime;
import top.linrty.live.common.domain.dto.pay.PayProductDTO;
import top.linrty.live.common.domain.dto.shop.SkuDetailInfoDTO;
import top.linrty.live.common.domain.dto.shop.SkuInfoDTO;
import top.linrty.live.common.domain.vo.shop.SkuInfoVO;
import top.linrty.live.common.enums.StatusEnum;
import top.linrty.live.common.utils.ConvertBeanUtils;
import top.linrty.live.shop.domain.po.SkuInfo;
import top.linrty.live.shop.mapper.SkuInfoMapper;
import top.linrty.live.shop.service.IAnchorShopInfoService;
import top.linrty.live.shop.service.ISkuInfoService;
import top.linrty.live.shop.utils.ShopProviderCacheKeyBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 17:49
 * @Version: 1.0
 **/
@Service
@Slf4j
public class SkuInfoServiceImpl implements ISkuInfoService {

    @Resource
    private SkuInfoMapper skuInfoMapper;

    @Resource
    private ShopProviderCacheKeyBuilder shopProviderCacheKeyBuilder;


    @Resource
    private IAnchorShopInfoService anchorShopInfoService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<SkuInfo> queryBySkuIds(List<Long> skuIdList) {
        LambdaQueryWrapper<SkuInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SkuInfo::getSkuId, skuIdList);
        queryWrapper.eq(SkuInfo::getStatus, StatusEnum.VALID_STATUS.getCode());
        return skuInfoMapper.selectList(queryWrapper);
    }

    @Override
    public SkuInfo queryBySkuId(Long skuId) {
        LambdaQueryWrapper<SkuInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SkuInfo::getSkuId, skuId);
        queryWrapper.eq(SkuInfo::getStatus, StatusEnum.VALID_STATUS.getCode());
        queryWrapper.last("limit 1");
        return skuInfoMapper.selectOne(queryWrapper);
    }

    @Override
    public List<SkuInfoVO> queryByAnchorId(Long anchorId) {
        // 先从缓存中获取主播下得商品信息
        String key = shopProviderCacheKeyBuilder.buildSkuDetailInfoMap(anchorId);
        List<SkuInfoVO> skuInfoVOList = redisTemplate.opsForHash().values(key).stream().map(x -> (SkuInfoVO) x).collect(Collectors.toList());
        if (!CollectionUtil.isEmpty(skuInfoVOList)){
            if (skuInfoVOList.get(0).getSkuId() == null){
                return Collections.emptyList();
            }
            return skuInfoVOList;
        }
        // 缓存中没有则从数据库中获取
        List<Long> skuIdList = anchorShopInfoService.querySkuIdsByAnchorId(anchorId);
        if (CollectionUtil.isEmpty(skuIdList)){
            return Collections.emptyList();
        }
        skuInfoVOList = ConvertBeanUtils.convertList(queryBySkuIds(skuIdList), SkuInfoVO.class);
        if (CollectionUtil.isEmpty(skuInfoVOList)){
            redisTemplate.opsForHash().put(key, -1, new PayProductDTO());
            redisTemplate.expire(key, RedisKeyTime.EXPIRE_TIME_ONE_MINUTE, TimeUnit.SECONDS);
            return Collections.emptyList();
        }
        // 使用redis缓存
        Map<String, SkuInfoVO> skuInfoVOMap = skuInfoVOList.stream().collect(Collectors.toMap(x -> String.valueOf(x.getSkuId()), x -> x));
        redisTemplate.opsForHash().putAll(key, skuInfoVOMap);
        redisTemplate.expire(key, RedisKeyTime.EXPIRE_TIME_HALF_HOUR, TimeUnit.SECONDS);
        return skuInfoVOList;
    }

    @Override
    public SkuDetailInfoDTO queryBySkuId(Long skuId, Long anchorId) {
        String cacheKey = shopProviderCacheKeyBuilder.buildSkuDetailInfoMap(anchorId);
        SkuInfoDTO skuInfoDTO = (SkuInfoDTO) redisTemplate.opsForHash().get(cacheKey, String.valueOf(skuId));
        if (skuInfoDTO != null) {
            return ConvertBeanUtils.convert(skuInfoDTO, SkuDetailInfoDTO.class);
        }
        skuInfoDTO = ConvertBeanUtils.convert(queryBySkuId(skuId), SkuInfoDTO.class);
        if (skuInfoDTO != null) {
            redisTemplate.opsForHash().put(cacheKey, String.valueOf(skuId), skuInfoDTO);
        }
        return ConvertBeanUtils.convert(skuInfoDTO, SkuDetailInfoDTO.class);
    }
}
