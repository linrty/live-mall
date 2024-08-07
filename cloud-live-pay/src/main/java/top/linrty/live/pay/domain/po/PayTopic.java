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
 * @Date: 2024/8/7 11:30
 * @Version: 1.0
 **/
@Data
@TableName("t_pay_topic")
@Accessors(chain = true)
public class PayTopic {

    @TableId(type = IdType.AUTO)
    @TableField(value = "id")
    @Schema(description = "id")
    private Long id;

    @TableField(value = "topic")
    @Schema(description = "主题")
    private String topic;

    @TableField(value = "status")
    @Schema(description = "状态")
    private Integer status;

    @TableField(value = "biz_code")
    @Schema(description = "业务类型")
    private Integer bizCode;

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
