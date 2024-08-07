package top.linrty.live.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import top.linrty.live.common.config.redis.RedisKeyTime;
import top.linrty.live.common.domain.dto.shop.RollBackStockDTO;
import top.linrty.live.common.domain.dto.shop.SkuOrderInfoReqDTO;
import top.linrty.live.common.domain.vo.shop.SkuOrderInfoRespVO;
import top.linrty.live.common.enums.StatusEnum;
import top.linrty.live.common.enums.shop.SkuOrderInfoEnum;
import top.linrty.live.shop.domain.po.SkuStockInfo;
import top.linrty.live.shop.mapper.SkuStockInfoMapper;
import top.linrty.live.shop.service.IAnchorShopInfoService;
import top.linrty.live.shop.service.ISkuInfoService;
import top.linrty.live.shop.service.ISkuOrderInfoService;
import top.linrty.live.shop.service.ISkuStockInfoService;
import top.linrty.live.shop.utils.ShopProviderCacheKeyBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 20:34
 * @Version: 1.0
 **/
@Service
@Slf4j
public class SkuStockInfoServiceImpl implements ISkuStockInfoService {

    @Resource
    private SkuStockInfoMapper skuStockInfoMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private ShopProviderCacheKeyBuilder shopProviderCacheKeyBuilder;


    @Resource
    private ISkuOrderInfoService skuOrderInfoService;

    @Resource
    private IAnchorShopInfoService anchorShopInfoService;

    @Resource
    private ISkuInfoService skuInfoService;

    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;
    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setLocation(new ClassPathResource("secKill.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);
    }

    @Override
    public boolean updateStockNum(Long skuId, Integer stockNum) {
        LambdaUpdateWrapper<SkuStockInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SkuStockInfo::getSkuId, skuId);
        SkuStockInfo skuStockInfoPO = new SkuStockInfo();
        skuStockInfoPO.setStockNum(stockNum);
        return skuStockInfoMapper.update(skuStockInfoPO, updateWrapper) > 0;
    }

    @Override
    public boolean decrStockNumBySkuId(Long skuId, Integer num) {
        return skuStockInfoMapper.decrStockNumBySkuId(skuId, num);
    }

    @Override
    public boolean decrStockNumBySkuIdByLua(Long skuId, Integer num) {
        return redisTemplate.execute(
                SECKILL_SCRIPT,
                Collections.singletonList(shopProviderCacheKeyBuilder.buildSkuStock(skuId)),
                num
        ) >= 0;
    }

    @Override
    public SkuStockInfo queryBySkuId(Long skuId) {
        LambdaQueryWrapper<SkuStockInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SkuStockInfo::getSkuId, skuId);
        queryWrapper.eq(SkuStockInfo::getStatus, StatusEnum.VALID_STATUS.getCode());
        queryWrapper.last("limit 1");
        return skuStockInfoMapper.selectOne(queryWrapper);
    }

    @Override
    public List<SkuStockInfo> queryBySkuIds(List<Long> skuIdList) {
        LambdaQueryWrapper<SkuStockInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SkuStockInfo::getSkuId, skuIdList);
        queryWrapper.eq(SkuStockInfo::getStatus, StatusEnum.VALID_STATUS.getCode());
        return skuStockInfoMapper.selectList(queryWrapper);
    }

    @Override
    public boolean syncStockNumToMySQL(Long anchor) {
        List<Long> skuIdList = anchorShopInfoService.querySkuIdsByAnchorId(anchor);
        for (Long skuId : skuIdList) {
            Integer stockNum = this.queryStockNum(skuId);
            if (stockNum != null) {
                updateStockNum(skuId, stockNum);
            }
        }
        return true;
    }

    @Override
    public void stockRollBackHandler(RollBackStockDTO rollBackStockDTO) {
        SkuOrderInfoRespVO respVO = skuOrderInfoService.queryByOrderId(rollBackStockDTO.getOrderId());
        if (respVO == null || !respVO.getStatus().equals(SkuOrderInfoEnum.PREPARE_PAY.getCode())) {
            return;
        }
        SkuOrderInfoReqDTO skuOrderInfoReqDTO = new SkuOrderInfoReqDTO();
        skuOrderInfoReqDTO.setStatus(SkuOrderInfoEnum.CANCEL.getCode());
        skuOrderInfoReqDTO.setId(rollBackStockDTO.getOrderId());
        // 设置订单状态未撤销状态
        skuOrderInfoService.updateOrderStatus(skuOrderInfoReqDTO);
        // 回滚库存
        List<Long> skuIdList = Arrays.stream(respVO.getSkuIdList().split(",")).map(Long::valueOf).collect(Collectors.toList());
        skuIdList.parallelStream().forEach(skuId -> {
            // 只用更新Redis库存，定时任务会自动更新MySQL库存
            redisTemplate.opsForValue().increment(shopProviderCacheKeyBuilder.buildSkuStock(skuId), 1);
        });
    }

    @Override
    public boolean prepareStockInfo(Long anchorId) {
        List<Long> skuIdList = anchorShopInfoService.querySkuIdsByAnchorId(anchorId);
        List<SkuStockInfo> skuStockInfos = queryBySkuIds(skuIdList);
        Map<String, Integer> cacheKeyMap = skuStockInfos.stream()
                .collect(Collectors.toMap(skuStockInfo -> shopProviderCacheKeyBuilder.buildSkuStock(skuStockInfo.getSkuId()), SkuStockInfo::getStockNum));
        redisTemplate.opsForValue().multiSet(cacheKeyMap);
        redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                for (String key : cacheKeyMap.keySet()) {
                    operations.expire((K) key, RedisKeyTime.EXPIRE_TIME_ONE_DAY, TimeUnit.SECONDS);
                }
                return null;
            }
        });
        return true;
    }

    @Override
    public Integer queryStockNum(Long skuId) {
        String cacheKey = shopProviderCacheKeyBuilder.buildSkuStock(skuId);
        Object stockObj = redisTemplate.opsForValue().get(cacheKey);
        return stockObj == null ? null : (Integer) stockObj;
    }
}
