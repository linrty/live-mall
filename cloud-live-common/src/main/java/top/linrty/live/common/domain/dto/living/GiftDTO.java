package top.linrty.live.common.domain.dto.living;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/6 23:19
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
public class GiftDTO {
    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "礼物id")
    private Integer giftId;

    @Schema(description = "礼物价格")
    private Integer price;

    @Schema(description = "接受者id")
    private Long receiverId;

    @Schema(description = "房间id")
    private Integer roomId;


    private String url;

    private String uuid;

    private Integer type;
}
