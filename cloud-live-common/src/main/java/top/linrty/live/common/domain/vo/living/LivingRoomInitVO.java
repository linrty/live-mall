package top.linrty.live.common.domain.vo.living;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/5 15:45
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
public class LivingRoomInitVO {

    @Schema(description = "主播id")
    private Long anchorId;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "主播头像")
    private String anchorImg;

    @Schema(description = "房间名")
    private String roomName;

    @Schema(description = "是否主播")
    private boolean isAnchor;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "房间id")
    private Integer roomId;

    @Schema(description = "观看者昵称")
    private String watcherNickName;

    @Schema(description = "主播昵称")
    private String anchorNickName;
    //观众头像
    @Schema(description = "观众头像")
    private String watcherAvatar;
    //默认背景图，为了方便讲解使用
    @Schema(description = "默认背景图")
    private String defaultBgImg;

    @Schema(description = "pk对象id")
    private Long pkObjId;

    @Schema(description = "用户昵称")
    private String nickName;

}
