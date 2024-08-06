package top.linrty.live.common.domain.dto.living;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/4 23:21
 * @Version: 1.0
 **/
@Data
public class LivingRoomRespDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -4370402310595190339L;

    @Schema(description = "房间id")
    private Integer id;

    @Schema(description = "主播id")
    private Long anchorId;

    @Schema(description = "房间名")
    private String roomName;

    @Schema(description = "封面图")
    private String covertImg;

    @Schema(description = "房间类型")
    private Integer type;

    @Schema(description = "观看人数")
    private Integer watchNum;

    @Schema(description = "点赞数")
    private Integer goodNum;

    @Schema(description = "房间状态")
    private Long pkObjId;
}
