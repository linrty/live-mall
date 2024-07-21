package top.linrty.live.user.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Description: 用户标签
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/21 23:12
 * @Version: 1.0
 **/
@TableName("t_user_tag")
@Data
public class UserTag {
    @TableId(type = IdType.INPUT)
    private Long userId;

    @TableField(value = "tag_info_01")
    private Long tagInfo01;

    @TableField(value = "tag_info_02")
    private Long tagInfo02;

    @TableField(value = "tag_info_03")
    private Long tagInfo03;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;
}
