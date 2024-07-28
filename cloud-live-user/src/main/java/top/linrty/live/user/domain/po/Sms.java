package top.linrty.live.user.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @Description: 记录短信相关信息
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/28 21:03
 * @Version: 1.0
 **/
@TableName("t_sms")
@Data
public class Sms {
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "验证码")
    @TableField("code")
    private Integer code;

    @Schema(description = "手机号")
    @TableField("phone")
    private String phone;

    @Schema(description = "发送时间")
    @TableField("send_time")
    private Date sendTime;

    @Schema(description = "更新时间")
    @TableField("update_time")
    private Date updateTime;
}
