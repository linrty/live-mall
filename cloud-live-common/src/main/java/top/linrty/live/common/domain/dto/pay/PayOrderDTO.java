package top.linrty.live.common.domain.dto.pay;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 1:55
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
public class PayOrderDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 7789861579943422191L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "订单号")
    private String orderId;

    @Schema(description = "产品id")
    private Integer productId;

    @Schema(description = "业务code")
    private Integer bizCode;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "来源")
    private Integer source;

    @Schema(description = "支付渠道")
    private Integer payChannel;

    @Schema(description = "订单状态")
    private Integer status;

    @Schema(description = "支付时间")
    private Date payTime;
}
