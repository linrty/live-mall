package top.linrty.live.common.domain.vo.shop;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import top.linrty.live.common.domain.dto.shop.SkuInfoDTO;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 16:57
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
public class ShopCarItemRespVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7247175817439564893L;

    @Schema(description = "数量")
    private Integer count;

    @Schema(description = "sku信息")
    private SkuInfoDTO skuInfoDTO;
}
