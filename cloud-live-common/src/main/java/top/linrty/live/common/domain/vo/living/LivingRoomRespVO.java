package top.linrty.live.common.domain.vo.living;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/5 16:43
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
public class LivingRoomRespVO {
    @Schema(description = "房间id")
    private Integer id;

    @Schema(description = "房间名")
    private String roomName;

    @Schema(description = "主播id")
    private Long anchorId;

    @Schema(description = "观看者数量")
    private Integer watchNum;

    @Schema(description = "点赞数量")
    private Integer goodNum;

    @Schema(description = "封面图片")
    private String covertImg;
}
