package top.linrty.live.common.domain.vo.living;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 16:23
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
public class RedPacketReceiveVO {

    @Schema(description = "红包价格")
    private Integer price;

    @Schema(description = "消息")
    private String msg;
}
