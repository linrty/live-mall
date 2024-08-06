package top.linrty.live.pay.service;

import top.linrty.live.common.domain.dto.pay.PayProductDTO;
import top.linrty.live.common.domain.dto.pay.PayProductReqDTO;
import top.linrty.live.common.domain.vo.pay.PayProductRespVO;

import java.util.List;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 1:25
 * @Version: 1.0
 **/
public interface IPayProductService {
    /**
     * 根据产品类型，返回批量的商品信息
     */
    List<PayProductDTO> products(Integer type);

    /**
     * 根据产品id查询
     */
    PayProductDTO getByProductId(Integer productId);

    /**
     * 发起支付
     */
    PayProductRespVO payProduct(PayProductReqDTO payProductReqDTO);
}
