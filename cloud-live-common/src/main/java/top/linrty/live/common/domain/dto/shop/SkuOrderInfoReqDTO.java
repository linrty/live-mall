package top.linrty.live.common.domain.dto.shop;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 16:58
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
public class SkuOrderInfoReqDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -9220028624463964600L;

    @Schema(description = "订单id")
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "房间id")
    private Integer roomId;

    @Schema(description = "订单状态")
    private Integer status;

    @Schema(description = "skuId列表")
    private List<Long> skuIdList;
}
