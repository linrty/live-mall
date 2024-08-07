package top.linrty.live.common.domain.vo.shop;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 16:59
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
public class SkuOrderInfoRespVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 2916280620499166681L;

    @Schema(description = "订单id")
    private Long Id;

    @Schema(description = "sku")
    private String skuIdList;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "房间id")
    private Integer roomId;

    @Schema(description = "订单状态")
    private Integer status;

    @Schema(description = "额外信息")
    private String extra;
}
