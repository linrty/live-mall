package top.linrty.live.shop.domain.po;

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
 * @Date: 2024/8/7 17:05
 * @Version: 1.0
 **/
@Data
@TableName("t_anchor_shop_info")
@Accessors(chain = true)
public class AnchorShopInfo {

    @TableId(type = IdType.AUTO)
    @TableField(value = "id")
    @Schema(description = "id")
    private Integer id;

    @TableField(value = "anchor_id")
    @Schema(description = "主播id")
    private Long anchorId;

    @TableField(value = "sku_id")
    @Schema(description = "skuId")
    private Long skuId;

    @TableField(value = "status")
    @Schema(description = "状态")
    private Integer status;

    @TableField(value = "create_time")
    @Schema(description = "创建时间")
    private Date createTime;

    @TableField(value = "update_time")
    @Schema(description = "更新时间")
    private Date updateTime;
}
