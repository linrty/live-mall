package top.linrty.live.shop.service;

import top.linrty.live.common.domain.dto.shop.RollBackStockDTO;
import top.linrty.live.shop.domain.po.SkuStockInfo;

import java.util.List;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 20:33
 * @Version: 1.0
 **/
public interface ISkuStockInfoService {
    /**
     * 根据stuId跟新库存之
     */
    boolean updateStockNum(Long skuId, Integer stockNum);

    /**
     * 根据stuId扣减库存值
     */
    boolean decrStockNumBySkuId(Long skuId, Integer num);

    /**
     * 使用lua脚本扣减缓存的库存值
     */
    boolean decrStockNumBySkuIdByLua(Long skuId, Integer num);

    /**
     * 根据skuId查询库存值
     */
    SkuStockInfo queryBySkuId(Long skuId);

    /**
     * 根据stuIdList批量查询数据
     */
    List<SkuStockInfo> queryBySkuIds(List<Long> skuIdList);

    /**
     * 同步库存到MySQL
     */
    boolean syncStockNumToMySQL(Long anchor);

    /**
     * 库存回滚
     */
    void stockRollBackHandler(RollBackStockDTO rollBackStockDTO);

    /**
     * 预热库存信息：将库存存入到Redis
     */
    boolean prepareStockInfo(Long anchorId);

    /**
     * 从Redis中查询缓存的库存值
     */
    Integer queryStockNum(Long skuId);
}
