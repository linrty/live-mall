package top.linrty.live.pay.controller;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.linrty.live.common.constants.HTTPKeyConstants;
import top.linrty.live.common.domain.dto.pay.PayProductDTO;
import top.linrty.live.common.domain.dto.pay.PayProductReqDTO;
import top.linrty.live.common.domain.vo.CommonRespVO;
import top.linrty.live.common.domain.vo.pay.PayProductItemVO;
import top.linrty.live.common.domain.vo.pay.PayProductVO;
import top.linrty.live.common.exception.ParamException;
import top.linrty.live.common.utils.RequestContext;
import top.linrty.live.pay.service.ICurrencyAccountService;
import top.linrty.live.pay.service.IPayProductService;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 1:36
 * @Version: 1.0
 **/
@RestController
@RequestMapping("/bank")
public class PayController {

    @Resource
    private IPayProductService payProductService;

    @Resource
    private ICurrencyAccountService currencyAccountService;

    @PostMapping("/products")
    public CommonRespVO products(Integer type) {
        if (type == null) {
            throw new ParamException("类型不能为空");
        }
        List<PayProductDTO> products = payProductService.products(type);
        List<PayProductItemVO> resItems = new ArrayList<>();
        for (PayProductDTO product : products) {
            PayProductItemVO item = new PayProductItemVO();
            item.setCoinNum(JSON.parseObject(product.getExtra()).getInteger("coin"));
            item.setId(product.getId());
            item.setName(product.getName());
            resItems.add(item);
        }
        PayProductVO payProductVO = new PayProductVO();
        String userIdStr = RequestContext.get(HTTPKeyConstants.HTTP_HEADER_USER_ID).toString();
        if (userIdStr == null) {
            throw new ParamException("用户id不能为空");
        }
        Long userId = Long.parseLong(userIdStr);
        payProductVO.setCurrentBalance(currencyAccountService.getBalance(userId));
        payProductVO.setPayProductItemVOList(resItems);
        return CommonRespVO.success().setData(payProductVO);
    }

    // 1.申请调用第三方支付接口（签名-》支付宝/微信）（生成一条支付中状态的订单）
    // 2.生成一个（特定的支付页）二维码（输入账户密码，支付）（第三方平台完成）
    // 3.发送回调请求-》业务方
    // 要求（可以接收不同平台的回调数据）
    // 可以根据业务标识去回调不同的业务服务（自定义参数组成中，塞入一个业务code,根据业务code去回调不同的业务服务）
    @PostMapping("/payProduct")
    public CommonRespVO payProduct(PayProductReqDTO payProductReqDTO) {
        return CommonRespVO.success().setData(payProductService.payProduct(payProductReqDTO));
    }
}
