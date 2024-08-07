package top.linrty.live.living.domain.po;

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
 * @Date: 2024/8/7 15:32
 * @Version: 1.0
 **/
@Data
@TableName("t_red_packet_config")
@Accessors(chain = true)
public class RedPacketConfig {
    @TableId(type = IdType.AUTO)
    @TableField(value = "id")
    @Schema(description = "id")
    private Integer id;

    @TableField(value = "anchor_id")
    @Schema(description = "主播id")
    private Long anchorId;

    @TableField(value = "start_time")
    @Schema(description = "开始时间")
    private Date startTime;

    @TableField(value = "total_get")
    @Schema(description = "一共领取数量")
    private Integer totalGet;

    @TableField(value = "total_get_price")
    @Schema(description = "一共领取金额")
    private Integer totalGetPrice;

    @TableField(value = "max_get_price")
    @Schema(description = "最大领取金额")
    private Integer maxGetPrice;

    @TableField(value = "status")
    @Schema(description = "状态")
    private Integer status;

    @TableField(value = "total_price")
    @Schema(description = "红包总金额")
    private Integer totalPrice;

    @TableField(value = "total_count")
    @Schema(description = "红包总数量")
    private Integer totalCount;

    @TableField(value = "config_code")
    @Schema(description = "红包配置码")
    private String configCode;

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
