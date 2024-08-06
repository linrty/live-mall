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
 * @Date: 2024/8/6 22:03
 * @Version: 1.0
 **/
@Data
@TableName("t_gift_config")
public class GiftConfig {
    @TableId(type = IdType.AUTO)
    @TableField(value = "gift_id")
    @Schema(description = "礼物id")
    private Integer giftId;

    @TableField(value = "price")
    @Schema(description = "礼物价格")
    private Integer price;

    @TableField(value = "gift_name")
    @Schema(description = "礼物名称")
    private String giftName;

    @TableField(value = "status")
    @Schema(description = "礼物状态")
    private Integer status;

    @TableField(value = "cover_img_url")
    @Schema(description = "礼物封面图")
    private String coverImgUrl;

    @TableField(value = "svga_url")
    @Schema(description = "礼物svga图")
    private String svgaUrl;

    @TableField(value = "create_time")
    @Schema(description = "礼物创建时间")
    private Date createTime;

    @TableField(value = "update_time")
    @Schema(description = "礼物更新时间")
    private Date updateTime;
}
