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
 * @Date: 2024/8/6 22:41
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
@TableName("t_currency_trade")
public class CurrencyTrade {

    @TableId(type = IdType.AUTO)
    @TableField(value = "id")
    @Schema(description = "id")
    private Long id;

    @TableField(value = "user_id")
    @Schema(description = "用户id")
    private Long userId;

    @TableField(value = "num")
    @Schema(description = "流水金额（单位：分）")
    private Integer num;

    @TableField(value = "type")
    @Schema(description = "流水类型")
    private int type;

    @TableField(value = "status")
    @Schema(description = "状态0无效1有效")
    private int status;

    @TableField(value = "create_time")
    @Schema(description = "创建时间")
    private Date createTime;

    @TableField(value = "update_time")
    @Schema(description = "更新时间")
    private Date updateTime;
}
