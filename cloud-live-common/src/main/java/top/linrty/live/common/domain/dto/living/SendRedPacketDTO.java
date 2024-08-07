package top.linrty.live.common.domain.dto.living;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Description: 用户红包雨抢红包后发送的mq消息体
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 16:12
 * @Version: 1.0
 **/
@Data
public class SendRedPacketDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1829802295999336708L;

    private Integer price;
    private RedPacketConfigReqDTO reqDTO;
}
