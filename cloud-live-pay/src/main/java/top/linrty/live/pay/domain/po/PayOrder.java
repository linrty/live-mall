package top.linrty.live.pay.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 1:50
 * @Version: 1.0
 **/
@Data
@TableName("t_pay_order")
@Accessors(chain = true)
public class PayOrder {
    @TableId(type = IdType.AUTO)
    @TableField(value = "id")
    @Schema(description = "订单id")
    private Long id;

    @TableField(value = "order_id")
    @Schema(description = "订单号")
    private String orderId;

    @TableField(value = "product_id")
    @Schema(description = "产品id")
    private Integer productId;

    @TableField(value = "user_id")
    @Schema(description = "用户id")
    private Long userId;

    @TableField(value = "source")
    @Schema(description = "来源")
    private Integer source;

    @TableField(value = "pay_channel")
    @Schema(description = "支付渠道")
    private Integer payChannel;

    @TableField(value = "status")
    @Schema(description = "订单状态")
    private Integer status;

    @TableField(value = "pay_time")
    @Schema(description = "支付时间")
    private Date payTime;

    @TableField(value = "create_time")
    @Schema(description = "创建时间")
    private Date createTime;

    @TableField(value = "update_time")
    @Schema(description = "更新时间")
    private Date updateTime;
}
