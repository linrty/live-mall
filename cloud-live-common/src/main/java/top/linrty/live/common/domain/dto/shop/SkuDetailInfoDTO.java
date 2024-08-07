package top.linrty.live.common.domain.dto.shop;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 16:55
 * @Version: 1.0
 **/
@Data
public class SkuDetailInfoDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -1279033925266285109L;

    @Schema(description = "skuId")
    private Long skuId;

    @Schema(description = "价格")
    private Integer skuPrice;

    @Schema(description = "sku编码")
    private String skuCode;

    @Schema(description = "sku名称")
    private String name;

    @Schema(description = "sku图标")
    private String iconUrl;

    @Schema(description = "sku原图标")
    private String originalIconUrl;

    @Schema(description = "sku描述")
    private String remark;
}
