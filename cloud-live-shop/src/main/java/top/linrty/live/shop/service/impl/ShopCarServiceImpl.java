package top.linrty.live.shop.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.linrty.live.common.constants.HTTPKeyConstants;
import top.linrty.live.common.domain.dto.shop.ShopCarReqDTO;
import top.linrty.live.common.domain.dto.shop.SkuInfoDTO;
import top.linrty.live.common.domain.vo.shop.ShopCarItemRespVO;
import top.linrty.live.common.domain.vo.shop.ShopCarRespVO;
import top.linrty.live.common.utils.ConvertBeanUtils;
import top.linrty.live.common.utils.RequestContext;
import top.linrty.live.shop.domain.po.SkuInfo;
import top.linrty.live.shop.service.IShopCarService;
import top.linrty.live.shop.service.ISkuInfoService;
import top.linrty.live.shop.utils.ShopProviderCacheKeyBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 20:27
 * @Version: 1.0
 **/
@Service
@Slf4j
public class ShopCarServiceImpl implements IShopCarService {


    @Resource
    private ShopProviderCacheKeyBuilder shopProviderCacheKeyBuilder;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @Resource
    private ISkuInfoService skuInfoService;



    /**
     * 因为是以直播间为维度的购物车，所以不需要持久化，用缓存即可
     */
    @Override
    public Boolean addCar(ShopCarReqDTO shopCarReqDTO) {
        Long userId = Long.parseLong(RequestContext.get(HTTPKeyConstants.HTTP_HEADER_USER_ID).toString());
        shopCarReqDTO.setUserId(userId);
        String cacheKey = shopProviderCacheKeyBuilder.buildUserShopCar(shopCarReqDTO.getUserId(), shopCarReqDTO.getRoomId());
        redisTemplate.opsForHash().put(cacheKey, String.valueOf(shopCarReqDTO.getSkuId()), 1);
        return true;
    }

    @Override
    public Boolean removeFromCar(ShopCarReqDTO shopCarReqDTO) {
        Long userId = Long.parseLong(RequestContext.get(HTTPKeyConstants.HTTP_HEADER_USER_ID).toString());
        shopCarReqDTO.setUserId(userId);
        String cacheKey = shopProviderCacheKeyBuilder.buildUserShopCar(shopCarReqDTO.getUserId(), shopCarReqDTO.getRoomId());
        redisTemplate.opsForHash().delete(cacheKey, String.valueOf(shopCarReqDTO.getSkuId()));
        return true;
    }

    @Override
    public Boolean clearShopCar(ShopCarReqDTO shopCarReqDTO) {
        Long userId = Long.parseLong(RequestContext.get(HTTPKeyConstants.HTTP_HEADER_USER_ID).toString());
        shopCarReqDTO.setUserId(userId);
        String cacheKey = shopProviderCacheKeyBuilder.buildUserShopCar(shopCarReqDTO.getUserId(), shopCarReqDTO.getRoomId());
        redisTemplate.delete(cacheKey);
        return true;
    }

    @Override
    public Boolean addCarItemNum(ShopCarReqDTO shopCarReqDTO) {
        Long userId = Long.parseLong(RequestContext.get(HTTPKeyConstants.HTTP_HEADER_USER_ID).toString());
        shopCarReqDTO.setUserId(userId);
        String cacheKey = shopProviderCacheKeyBuilder.buildUserShopCar(shopCarReqDTO.getUserId(), shopCarReqDTO.getRoomId());
        redisTemplate.opsForHash().increment(cacheKey, String.valueOf(shopCarReqDTO.getSkuId()), 1);
        return true;
    }

    @Override
    public ShopCarRespVO getCarInfo(ShopCarReqDTO shopCarReqDTO) {
        Long userId = Long.parseLong(RequestContext.get(HTTPKeyConstants.HTTP_HEADER_USER_ID).toString());
        shopCarReqDTO.setUserId(userId);
        String cacheKey = shopProviderCacheKeyBuilder.buildUserShopCar(shopCarReqDTO.getUserId(), shopCarReqDTO.getRoomId());
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(cacheKey);
        if (CollectionUtil.isEmpty(entries)) {
            return new ShopCarRespVO();
        }
        Map<Long, Integer> skuCountMap = new HashMap<>(entries.size());
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            skuCountMap.put(Long.valueOf((String) entry.getKey()), (Integer) entry.getValue());
        }
        List<Long> skuIdList = new ArrayList<>(skuCountMap.keySet());
        List<SkuInfo> skuInfoPOS = skuInfoService.queryBySkuIds(skuIdList);
        ShopCarRespVO shopCarRespVO = new ShopCarRespVO();
        shopCarRespVO.setRoomId(shopCarReqDTO.getRoomId());
        shopCarRespVO.setUserId(shopCarReqDTO.getUserId());
        List<ShopCarItemRespVO> itemList = new ArrayList<>();
        skuInfoPOS.forEach(skuInfoPO -> {
            ShopCarItemRespVO item = new ShopCarItemRespVO();
            item.setSkuInfoDTO(ConvertBeanUtils.convert(skuInfoPO, SkuInfoDTO.class));
            item.setCount(skuCountMap.get(skuInfoPO.getSkuId()));
            itemList.add(item);
        });
        shopCarRespVO.setShopCarItemRespVOS(itemList);
        return shopCarRespVO;
    }
}
