package top.linrty.live.common.domain.dto.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 2:07
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
public class PayProductReqDTO {
    // 产品id
    @Schema(description = "产品id")
    private Integer productId;
    /**
     * 支付来源（直播间内，用户中心），用于统计支付页面来源
     */
    @Schema(description = "支付来源")
    private Integer paySource;
    /**
     * 支付渠道
     */
    @Schema(description = "支付渠道")
    private Integer payChannel;
}
