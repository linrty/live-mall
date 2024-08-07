package top.linrty.live.shop.service;

import java.util.List;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 21:16
 * @Version: 1.0
 **/
public interface IAnchorShopInfoService {
    /**
     * 根据anchorId查询skuIdList
     */
    List<Long> querySkuIdsByAnchorId(Long anchorId);

    /**
     * 查询所有有效的主播id列表
     */
    List<Long> queryAllValidAnchorId();
}
