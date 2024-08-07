package top.linrty.live.common.domain.dto.living;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 15:36
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
public class RedPacketReceiveDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -5916608127876611063L;

    @Schema(description = "金额")
    private Integer price;

    @Schema(description = "消息")
    private String notifyMsg;
}
