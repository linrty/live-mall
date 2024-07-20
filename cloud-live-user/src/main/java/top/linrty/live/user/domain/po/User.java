package top.linrty.live.user.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
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
public class User {

    @Id
    @TableId(type = IdType.INPUT)
    private Long userId;

    @TableField("nick_name")
    private String nickName;

    @TableField("true_name")
    private String trueName;

    @TableField("avatar")
    private String avatar;

    @TableField("sex")
    private Integer sex;

    @TableField("work_city")
    private Integer workCity;

    @TableField("born_city")
    private Integer bornCity;

    @TableField("born_date")
    private Date bornDate;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;
}
