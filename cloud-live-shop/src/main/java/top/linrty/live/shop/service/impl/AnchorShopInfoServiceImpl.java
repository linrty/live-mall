package top.linrty.live.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import top.linrty.live.common.enums.StatusEnum;
import top.linrty.live.shop.domain.po.AnchorShopInfo;
import top.linrty.live.shop.mapper.AnchorShopInfoMapper;
import top.linrty.live.shop.service.IAnchorShopInfoService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 21:16
 * @Version: 1.0
 **/
@Service
public class AnchorShopInfoServiceImpl implements IAnchorShopInfoService {

    @Resource
    private AnchorShopInfoMapper anchorShopInfoMapper;
    @Override
    public List<Long> querySkuIdsByAnchorId(Long anchorId) {
        LambdaQueryWrapper<AnchorShopInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AnchorShopInfo::getAnchorId, anchorId);
        queryWrapper.eq(AnchorShopInfo::getStatus, StatusEnum.VALID_STATUS.getCode());
        queryWrapper.select(AnchorShopInfo::getSkuId);
        return anchorShopInfoMapper.selectList(queryWrapper).stream().map(AnchorShopInfo::getSkuId).collect(Collectors.toList());
    }

    @Override
    public List<Long> queryAllValidAnchorId() {
        LambdaQueryWrapper<AnchorShopInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AnchorShopInfo::getStatus, StatusEnum.VALID_STATUS.getCode());
        return anchorShopInfoMapper.selectList(queryWrapper).stream().map(AnchorShopInfo::getAnchorId).collect(Collectors.toList());
    }
}
