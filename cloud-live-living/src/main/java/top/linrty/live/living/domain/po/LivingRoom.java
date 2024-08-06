package top.linrty.live.living.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @Description: 正在直播的直播间表
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/4 23:27
 * @Version: 1.0
 **/
@TableName("t_living_room")
@Data
@Accessors(chain = true)
public class LivingRoom {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("anchor_id")
    private Long anchorId;

    @TableField("type")
    private Integer type;

    @TableField("room_name")
    private String roomName;

    @TableField("covert_img")
    private String covertImg;

    @TableField("status")
    private Integer status;

    @TableField("watch_num")
    private Integer watchNum;

    @TableField("good_num")
    private Integer goodNum;

    @TableField("start_time")
    private Date startTime;

    @TableField("update_time")
    private Date updateTime;
}
