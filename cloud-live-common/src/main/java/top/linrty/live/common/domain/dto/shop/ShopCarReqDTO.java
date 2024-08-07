package top.linrty.live.common.domain.dto.shop;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 16:52
 * @Version: 1.0
 **/
@Data
public class ShopCarReqDTO {

    @Schema(description = "skuId")
    private Long skuId;

    @Schema(description = "房间id")
    private Integer roomId;

    @Schema(description = "用户id")
    private Long userId;
}
