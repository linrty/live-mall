package top.linrty.live.living.domain.po;

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
 * @Date: 2024/8/6 22:05
 * @Version: 1.0
 **/
@Data
@TableName("t_gift_record")
public class GiftRecord {
    @TableId(type = IdType.AUTO)
    @TableField(value = "id")
    @Schema(description = "礼物记录id")
    private Long id;

    @TableField(value = "user_id")
    @Schema(description = "用户id")
    private Long userId;

    @TableField(value = "object_id")
    @Schema(description = "对象id")
    private Long objectId;

    @TableField(value = "source")
    @Schema(description = "礼物来源")
    private Integer source;

    @TableField(value = "price")
    @Schema(description = "礼物价格")
    private Integer price;

    @TableField(value = "price_unit")
    @Schema(description = "礼物单位")
    private Integer priceUnit;

    @TableField(value = "gift_id")
    @Schema(description = "礼物id")
    private Integer giftId;

    @TableField(value = "send_time")
    @Schema(description = "礼物发送时间")
    private Date sendTime;
}
