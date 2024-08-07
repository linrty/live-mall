package top.linrty.live.common.domain.vo.shop;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 17:03
 * @Version: 1.0
 **/
@Data
public class SkuPrepareOrderInfoVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -8683020132073931910L;

    @Schema(description = "总价")
    private Integer totalPrice;


    @Schema(description = "sku订单信息")
    private List<ShopCarItemRespVO> skuPrepareOrderItemInfoVOS;
}
