package top.linrty.live.common.domain.vo.im;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/6 16:55
 * @Version: 1.0
 **/
@Data
public class IMConfigVO {

    @Schema(description = "token")
    private String token;

    @Schema(description = "websocket地址")
    private String wsImServerAddress;

    @Schema(description = "tcp地址")
    private String tcpImServerAddress;
}
