package top.linrty.live.shop.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.linrty.live.common.domain.dto.shop.PrepareOrderDTO;
import top.linrty.live.common.domain.dto.shop.ShopCarReqDTO;
import top.linrty.live.common.domain.vo.CommonRespVO;
import top.linrty.live.shop.service.IShopCarService;
import top.linrty.live.shop.service.ISkuOrderInfoService;
import top.linrty.live.shop.service.ISkuStockInfoService;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 17:14
 * @Version: 1.0
 **/
@RestController
@RequestMapping("/shop")
public class ShopInfoController {

    @Resource
    private IShopCarService shopCarService;

    @Resource
    private ISkuOrderInfoService skuOrderInfoService;

    @Resource
    private ISkuStockInfoService skuStockInfoService;

    @PostMapping("/addCar")
    public CommonRespVO addCar(ShopCarReqDTO reqDTO) {
        return CommonRespVO.success().setData(shopCarService.addCar(reqDTO));
    }

    @PostMapping("/removeFromCar")
    public CommonRespVO removeFromCar(ShopCarReqDTO reqDTO) {
        return CommonRespVO.success().setData(shopCarService.removeFromCar(reqDTO));
    }

    @PostMapping("/getCarInfo")
    public CommonRespVO getCarInfo(ShopCarReqDTO reqDTO) {
        return CommonRespVO.success().setData(shopCarService.getCarInfo(reqDTO));
    }

    @PostMapping("/clearCar")
    public CommonRespVO clearCar(ShopCarReqDTO reqDTO) {
        return CommonRespVO.success().setData(shopCarService.clearShopCar(reqDTO));
    }

    @PostMapping("/prepareOrder")
    public CommonRespVO prepareOrder(PrepareOrderDTO prepareOrderDTO) {
        return CommonRespVO.success().setData(skuOrderInfoService.prepareOrder(prepareOrderDTO));
    }

    @PostMapping("/prepareStock")
    public CommonRespVO prepareStock(Long anchorId) {
        return CommonRespVO.success().setData(skuStockInfoService.prepareStockInfo(anchorId));
    }


    @PostMapping("/payNow")
    public CommonRespVO payNow(PrepareOrderDTO prepareOrderDTO) {
        return CommonRespVO.success().setData(skuOrderInfoService.payNow(prepareOrderDTO));
    }
}
