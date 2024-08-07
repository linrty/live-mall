package top.linrty.live.shop.service;

import top.linrty.live.common.domain.dto.shop.ShopCarReqDTO;
import top.linrty.live.common.domain.vo.shop.ShopCarRespVO;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 20:26
 * @Version: 1.0
 **/
public interface IShopCarService {
    /**
     * 添加商品到购物车中
     */
    Boolean addCar(ShopCarReqDTO shopCarReqDTO);

    /**
     * 移除购物车
     */
    Boolean removeFromCar(ShopCarReqDTO shopCarReqDTO);

    /**
     * 清空购物车
     */
    Boolean clearShopCar(ShopCarReqDTO shopCarReqDTO);

    /**
     * 修改购物车中某个商品的数量
     */
    Boolean addCarItemNum(ShopCarReqDTO shopCarReqDTO);

    /**
     * 查看购物车信息
     */
    ShopCarRespVO getCarInfo(ShopCarReqDTO shopCarReqDTO);
}
