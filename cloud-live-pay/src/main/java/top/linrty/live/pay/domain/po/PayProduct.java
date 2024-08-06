package top.linrty.live.pay.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 1:23
 * @Version: 1.0
 **/
@Data
@TableName("t_pay_product")
public class PayProduct {
    @TableId(type = IdType.AUTO)
    @TableField(value = "id")
    @Schema(description = "id")
    private Long id;

    @TableField(value = "name")
    @Schema(description = "名称")
    private String name;

    @TableField(value = "price")
    @Schema(description = "价格")
    private Integer price;

    @TableField(value = "extra")
    @Schema(description = "额外信息")
    private String extra;

    @TableField(value = "type")
    @Schema(description = "类型")
    private Integer type;

    @TableField(value = "valid_status")
    @Schema(description = "状态")
    private Integer validStatus;

    @TableField(value = "create_time")
    @Schema(description = "创建时间")
    private Date createTime;

    @TableField(value = "update_time")
    @Schema(description = "更新时间")
    private Date updateTime;
}
