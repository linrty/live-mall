package top.linrty.live.user.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperties;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/20 17:08
 * @Version: 1.0
 **/
@TableName("t_user")
@Data
@Accessors(chain = true)
public class User {

    @Id
    @Schema(description = "用户id")
    @TableId(type = IdType.INPUT)
    private Long userId;

    @Schema(description = "用户手机号")
    @TableField("phone")
    private String phone;

    @Schema(description = "用户昵称")
    @TableField("nick_name")
    private String nickName;

    @Schema(description = "真实姓名")
    @TableField("true_name")
    private String trueName;

    @Schema(description = "头像")
    @TableField("avatar")
    private String avatar;

    @Schema(description = "性别")
    @TableField("sex")
    private Integer sex;

    @Schema(description = "工作城市")
    @TableField("work_city")
    private Integer workCity;

    @Schema(description = "出生城市")
    @TableField("born_city")
    private Integer bornCity;

    @Schema(description = "出生日期")
    @TableField("born_date")
    private Date bornDate;

    @Schema(description = "创建时间")
    @TableField("create_time")
    private Date createTime;

    @Schema(description = "更新时间")
    @TableField("update_time")
    private Date updateTime;
}
