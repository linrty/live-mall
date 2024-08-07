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
 * @Date: 2024/8/7 15:34
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
public class RedPacketConfigReqDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 5117539613836783248L;

    @Schema(description = "id")
    private Integer id;

    @Schema(description = "房间id")
    private Integer roomId;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "红包配置码")
    private String redPacketConfigCode;

    @Schema(description = "红包总金额")
    private Integer totalPrice;

    @Schema(description = "红包总数量")
    private Integer totalCount;

    @Schema(description = "备注")
    private String remark;
}
