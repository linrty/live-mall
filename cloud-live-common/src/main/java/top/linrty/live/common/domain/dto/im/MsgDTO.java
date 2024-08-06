package top.linrty.live.common.domain.dto.im;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/3 23:05
 * @Version: 1.0
 **/
@Data
public class MsgDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1259190053670615404L;

    /**
     * 己方用户id（也是发送方用户id）
     */
    private Long userId;

    /**
     * 通信目标用户id
     */
    // private Long objectId;

    private Integer roomId;

    /**
     * 消息类型
     */
    private Integer type;
    /**
     * 消息内容
     */
    private String content;

    private Date createTime;

    private Date updateTime;

    /**
     * 发送人名称
     */
    private String senderName;
    /**
     * 发送人头像
     */
    private String senderAvatar;
}
