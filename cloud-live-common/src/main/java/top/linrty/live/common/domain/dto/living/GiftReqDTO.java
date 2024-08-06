package top.linrty.live.common.domain.dto.living;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/6 22:13
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
public class GiftReqDTO {

    @Schema(description = "礼物id")
    private int giftId;

    @Schema(description = "房间id")
    private Integer roomId;

    @Schema(description = "发送者id")
    private Long senderUserId;

    @Schema(description = "接受者id")
    private Long receiverId;

    @Schema(description = "类型")
    private Integer type;
}
