package top.linrty.live.shop.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import top.linrty.live.api.clients.PayClient;
import top.linrty.live.common.constants.HTTPKeyConstants;
import top.linrty.live.common.constants.living.GiftTopicNames;
import top.linrty.live.common.domain.dto.shop.PrepareOrderDTO;
import top.linrty.live.common.domain.dto.shop.RollBackStockDTO;
import top.linrty.live.common.domain.dto.shop.ShopCarReqDTO;
import top.linrty.live.common.domain.dto.shop.SkuOrderInfoReqDTO;
import top.linrty.live.common.domain.vo.shop.ShopCarItemRespVO;
import top.linrty.live.common.domain.vo.shop.ShopCarRespVO;
import top.linrty.live.common.domain.vo.shop.SkuOrderInfoRespVO;
import top.linrty.live.common.domain.vo.shop.SkuPrepareOrderInfoVO;
import top.linrty.live.common.enums.shop.SkuOrderInfoEnum;
import top.linrty.live.common.utils.ConvertBeanUtils;
import top.linrty.live.common.utils.RequestContext;
import top.linrty.live.shop.domain.po.SkuInfo;
import top.linrty.live.shop.domain.po.SkuOrderInfo;
import top.linrty.live.shop.mapper.SkuOrderInfoMapper;
import top.linrty.live.shop.service.IShopCarService;
import top.linrty.live.shop.service.ISkuInfoService;
import top.linrty.live.shop.service.ISkuOrderInfoService;
import top.linrty.live.shop.service.ISkuStockInfoService;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 21:21
 * @Version: 1.0
 **/
@Service
public class SkuOrderInfoServiceImpl implements ISkuOrderInfoService {

    @Resource
    private SkuOrderInfoMapper skuOrderInfoMapper;

    @Resource
    private IShopCarService shopCarService;

    @Resource
    private ISkuStockInfoService skuStockInfoService;

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Resource
    private ISkuInfoService skuInfoService;

    @Resource
    private PayClient payClient;


    @Override
    public SkuOrderInfoRespVO queryByUserIdAndRoomId(Long userId, Integer roomId) {
        LambdaQueryWrapper<SkuOrderInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SkuOrderInfo::getUserId, userId);
        queryWrapper.eq(SkuOrderInfo::getRoomId, roomId);
        queryWrapper.orderByDesc(SkuOrderInfo::getId);
        queryWrapper.last("limit 1");
        return ConvertBeanUtils.convert(skuOrderInfoMapper.selectOne(queryWrapper), SkuOrderInfoRespVO.class);
    }

    @Override
    public SkuOrderInfo insertOne(SkuOrderInfoReqDTO skuOrderInfoReqDTO) {
        String skuIdListStr = StrUtil.join(",", skuOrderInfoReqDTO.getSkuIdList());
        SkuOrderInfo skuOrderInfo = ConvertBeanUtils.convert(skuOrderInfoReqDTO, SkuOrderInfo.class);
        skuOrderInfo.setSkuIdList(skuIdListStr);
        skuOrderInfoMapper.insert(skuOrderInfo);
        return skuOrderInfo;
    }

    @Override
    public boolean updateOrderStatus(SkuOrderInfoReqDTO skuOrderInfoReqDTO) {
        SkuOrderInfo skuOrderInfo = new SkuOrderInfo();
        skuOrderInfo.setStatus(skuOrderInfoReqDTO.getStatus());
        skuOrderInfo.setId(skuOrderInfoReqDTO.getId());
        skuOrderInfoMapper.updateById(skuOrderInfo);
        return true;
    }

    @Override
    public SkuOrderInfoRespVO queryByOrderId(Long orderId) {
        return ConvertBeanUtils.convert(skuOrderInfoMapper.selectById(orderId), SkuOrderInfoRespVO.class);
    }

    @Override
    public SkuPrepareOrderInfoVO prepareOrder(PrepareOrderDTO reqDTO) {
        Long userId = Long.parseLong(RequestContext.get(HTTPKeyConstants.HTTP_HEADER_USER_ID).toString());
        reqDTO.setUserId(userId);
        ShopCarReqDTO shopCarReqDTO = ConvertBeanUtils.convert(reqDTO, ShopCarReqDTO.class);
        ShopCarRespVO carInfo = shopCarService.getCarInfo(shopCarReqDTO);
        List<ShopCarItemRespVO> carItemList = carInfo.getShopCarItemRespVOS();
        if (CollectionUtil.isEmpty(carItemList)) {
            return new SkuPrepareOrderInfoVO();
        }
        List<Long> skuIdList = carItemList.stream().map(item -> item.getSkuInfoDTO().getSkuId()).collect(Collectors.toList());
        Iterator<Long> iterator = skuIdList.iterator();
        // 进行商品库存的扣减
        while (iterator.hasNext()) {
            Long skuId = iterator.next();
            boolean isSuccess = skuStockInfoService.decrStockNumBySkuIdByLua(skuId, 1);
            if (!isSuccess) iterator.remove();
        }
        SkuOrderInfo skuOrderInfo = insertOne(
                new SkuOrderInfoReqDTO()
                        .setUserId(reqDTO.getUserId())
                        .setRoomId(reqDTO.getRoomId())
                        .setStatus(SkuOrderInfoEnum.PREPARE_PAY.getCode())
                        .setSkuIdList(skuIdList)
        );
        // 清空购物车
        shopCarService.clearShopCar(shopCarReqDTO);
        // 发送延时MQ：若订单未支付，进行库存回滚
        RollBackStockDTO rollBackStockBO =
                new RollBackStockDTO().setUserId(reqDTO.getUserId()).setOrderId(skuOrderInfo.getId());
        CompletableFuture<SendResult<String, String>> sendResult = kafkaTemplate.send(GiftTopicNames.ROLL_BACK_STOCK, JSON.toJSONString(rollBackStockBO));
        System.out.println(sendResult);

        // 封装返回对象
        SkuPrepareOrderInfoVO respDTO = new SkuPrepareOrderInfoVO();
        List<ShopCarItemRespVO> itemList = carItemList.stream().filter(item -> skuIdList.contains(item.getSkuInfoDTO().getSkuId())).collect(Collectors.toList());
        respDTO.setSkuPrepareOrderItemInfoVOS(itemList);
        respDTO.setTotalPrice(itemList.stream().map(item -> item.getSkuInfoDTO().getSkuPrice()).reduce(Integer::sum).orElse(0));
        return respDTO;
    }

    @Override
    public boolean payNow(PrepareOrderDTO prepareOrderDTO) {
        Long userId = Long.parseLong(RequestContext.get(HTTPKeyConstants.HTTP_HEADER_USER_ID).toString());
        SkuOrderInfoRespVO skuOrderInfo = queryByUserIdAndRoomId(userId, prepareOrderDTO.getRoomId());
        // 判断是否是未支付状态
        if (!skuOrderInfo.getStatus().equals(SkuOrderInfoEnum.PREPARE_PAY.getCode())) {
            return false;
        }
        // 获取到订单中的skuIdList
        List<Long> skuIdList = Arrays.stream(skuOrderInfo.getSkuIdList().split(",")).map(Long::valueOf).collect(Collectors.toList());
        List<SkuInfo> skuInfoPOS = skuInfoService.queryBySkuIds(skuIdList);
        // 计算出商品的总价
        Integer totalPrice = skuInfoPOS.stream().map(SkuInfo::getSkuPrice).reduce(Integer::sum).orElse(0);
        // 获取余额并判断余额是否充足
        Integer balance = payClient.getBalance(userId);
        if (balance < totalPrice) {
            return false;
        }
        // 余额扣减
        payClient.decrCurrencyAccount(userId, totalPrice);
        // 更改订单状态未已支付
        SkuOrderInfoReqDTO reqDTO = ConvertBeanUtils.convert(skuOrderInfo, SkuOrderInfoReqDTO.class);
        reqDTO.setStatus(SkuOrderInfoEnum.HAS_PAY.getCode());
        updateOrderStatus(reqDTO);
        return true;
    }
}
