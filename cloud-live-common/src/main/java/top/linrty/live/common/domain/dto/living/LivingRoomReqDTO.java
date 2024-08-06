package top.linrty.live.common.domain.dto.living;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Description: 直播间相关请求DTO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/4 23:15
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
public class LivingRoomReqDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -4370401310595190339L;

    @Schema(description = "房间id")
    private Integer id;

    @Schema(description = "主播id")
    private Long anchorId;

    @Schema(description = "房间类型")
    private Long pkObjId;

    @Schema(description = "房间名称")
    private String roomName;

    @Schema(description = "房间号")
    private Integer roomId;

    @Schema(description = "封面图片")
    private String covertImg;

    @Schema(description = "房间类型")
    private Integer type;

    @Schema(description = "appId")
    private Integer appId;

    @Schema(description = "页码")
    private int page;

    @Schema(description = "每页数量")
    private int pageSize;
}
