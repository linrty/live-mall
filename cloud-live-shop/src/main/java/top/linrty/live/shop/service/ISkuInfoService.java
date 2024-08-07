package top.linrty.live.shop.service;

import top.linrty.live.common.domain.dto.shop.SkuDetailInfoDTO;
import top.linrty.live.common.domain.dto.shop.SkuInfoDTO;
import top.linrty.live.common.domain.vo.shop.SkuInfoVO;
import top.linrty.live.shop.domain.po.SkuInfo;

import java.util.List;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 17:49
 * @Version: 1.0
 **/
public interface ISkuInfoService {
    /**
     * 使用skuIdList进行批量查询
     */
    List<SkuInfo> queryBySkuIds(List<Long> skuIdList);

    /**
     * 直接将SkuInfo当成SkuDetailInfo，根据skuId查询Info
     */
    SkuInfo queryBySkuId(Long skuId);

    List<SkuInfoVO> queryByAnchorId(Long anchorId);

    SkuDetailInfoDTO queryBySkuId(Long skuId, Long anchorId);
}
