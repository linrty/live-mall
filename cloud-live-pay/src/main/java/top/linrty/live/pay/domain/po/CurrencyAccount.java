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
 * @Description: 平台虚拟货币账户
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/6 22:39
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
@TableName("t_currency_account")
public class CurrencyAccount {

    @TableId(type = IdType.INPUT)
    @TableField(value = "user_id")
    @Schema(description = "用户id")
    private Long userId;

    @TableField(value = "current_balance")
    @Schema(description = "当前余额")
    private int currentBalance;

    @TableField(value = "total_charged")
    @Schema(description = "累计充值")
    private int totalCharged;

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
