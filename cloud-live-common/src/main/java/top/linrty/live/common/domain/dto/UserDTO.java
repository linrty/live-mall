package top.linrty.live.common.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 用户信息传输对象
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/17 23:41
 * @Version: 1.0
 **/
@Data
@Schema(name = "UserDTO", description = "用户信息传输对象")
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 4079363033445460398L;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "用户昵称")
    private String nickName;

    @Schema(description = "真实姓名")
    private String trueName;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "性别")
    private Integer sex;

    @Schema(description = "工作城市")
    private Integer workCity;

    @Schema(description = "出生城市")
    private Integer bornCity;

    @Schema(description = "出生日期")
    private Date bornDate;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;
}
