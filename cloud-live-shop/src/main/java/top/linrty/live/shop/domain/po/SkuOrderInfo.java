package top.linrty.live.shop.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 17:10
 * @Version: 1.0
 **/
@Data
@TableName("t_sku_order_info")
@Accessors(chain = true)
public class SkuOrderInfo {

    @TableId(type = IdType.AUTO)
    @TableField(value = "id")
    @Schema(description = "id")
    private Long id;

    @TableField(value = "sku_id_list")
    @Schema(description = "sku")
    private String skuIdList;

    @TableField(value = "user_id")
    @Schema(description = "用户id")
    private Long userId;

    @TableField(value = "room_id")
    @Schema(description = "房间id")
    private Integer roomId;

    @TableField(value = "status")
    @Schema(description = "订单状态")
    private Integer status;

    @TableField(value = "extra")
    @Schema(description = "额外信息")
    private String extra;

    @TableField(value = "create_time")
    @Schema(description = "创建时间")
    private Date createTime;

    @TableField(value = "update_time")
    @Schema(description = "更新时间")
    private Date updateTime;

}
