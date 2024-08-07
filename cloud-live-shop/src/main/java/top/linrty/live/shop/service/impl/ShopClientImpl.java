package top.linrty.live.shop.service.impl;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import top.linrty.live.api.clients.ShopClient;
import top.linrty.live.shop.service.ISkuStockInfoService;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 22:27
 * @Version: 1.0
 **/
@DubboService
public class ShopClientImpl implements ShopClient {

    @Resource
    private ISkuStockInfoService skuStockInfoService;

    @Override
    public boolean prepareStockInfo(Long anchorId) {
        return skuStockInfoService.prepareStockInfo(anchorId);
    }
}
