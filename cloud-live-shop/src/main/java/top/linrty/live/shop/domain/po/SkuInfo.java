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
 * @Date: 2024/8/7 17:04
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
@TableName("t_sku_info")
public class SkuInfo {
    @TableId(type = IdType.AUTO)
    @TableField(value = "id")
    @Schema(description = "id")
    private Long id;

    @TableField(value = "sku_id")
    @Schema(description = "skuId")
    private Long skuId;

    @TableField(value = "sku_price")
    @Schema(description = "sku价格")
    private Integer skuPrice;

    @TableField(value = "sku_code")
    @Schema(description = "sku编码")
    private String skuCode;

    @TableField(value = "name")
    @Schema(description = "sku名称")
    private String name;

    @TableField(value = "icon_url")
    @Schema(description = "sku图标")
    private String iconUrl;

    @TableField(value = "original_icon_url")
    @Schema(description = "原始图标")
    private String originalIconUrl;

    @TableField(value = "status")
    @Schema(description = "状态")
    private Integer status;

    @TableField(value = "remark")
    @Schema(description = "备注")
    private String remark;

    @TableField(value = "create_time")
    @Schema(description = "创建时间")
    private Date createTime;

    @TableField(value = "update_time")
    @Schema(description = "更新时间")
    private Date updateTime;
}
