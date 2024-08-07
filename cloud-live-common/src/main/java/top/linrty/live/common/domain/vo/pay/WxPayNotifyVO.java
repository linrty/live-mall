package top.linrty.live.common.domain.vo.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 11:28
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
public class WxPayNotifyVO {
    @Schema(description = "订单id")
    private String orderId;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "业务类型")
    private Integer bizCode;
}
