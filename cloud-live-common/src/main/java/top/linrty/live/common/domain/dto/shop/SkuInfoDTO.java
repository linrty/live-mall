package top.linrty.live.common.domain.dto.shop;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 16:54
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
public class SkuInfoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 6682046584901973187L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "skuId")
    private Long skuId;

    @Schema(description = "sku价格")
    private Integer skuPrice;

    @Schema(description = "sku编码")
    private String skuCode;

    @Schema(description = "sku名称")
    private String name;

    @Schema(description = "sku图标")
    private String iconUrl;

    @Schema(description = "sku原图标")
    private String originalIconUrl;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "sku描述")
    private String remark;
}
