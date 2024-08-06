package top.linrty.live.common.domain.dto.im;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/6 16:00
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
public class IMOfflineDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 5005291896846703608L;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "应用id")
    private Integer appId;

    @Schema(description = "房间id")
    private Integer roomId;

    @Schema(description = "下线时间")
    private long logoutTime;
}
