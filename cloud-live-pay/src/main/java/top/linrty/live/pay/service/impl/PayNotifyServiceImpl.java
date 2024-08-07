package top.linrty.live.pay.service.impl;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.linrty.live.common.domain.dto.pay.PayOrderDTO;
import top.linrty.live.common.domain.vo.pay.WxPayNotifyVO;
import top.linrty.live.pay.service.IPayNotifyService;
import top.linrty.live.pay.service.IPayOrderService;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 11:26
 * @Version: 1.0
 **/
@Service
@Slf4j
public class PayNotifyServiceImpl implements IPayNotifyService {

    @Resource
    private IPayOrderService payOrderService;

    @Override
    public String notifyHandler(String paramJson) {
        WxPayNotifyVO wxPayNotifyVO = JSON.parseObject(paramJson, WxPayNotifyVO.class);
        PayOrderDTO payOrderDTO = new PayOrderDTO();
        payOrderDTO.setUserId(wxPayNotifyVO.getUserId());
        payOrderDTO.setOrderId(wxPayNotifyVO.getOrderId());
        payOrderDTO.setBizCode(wxPayNotifyVO.getBizCode());
        return payOrderService.payNotify(payOrderDTO) ? "success" : "fail";
    }
}
