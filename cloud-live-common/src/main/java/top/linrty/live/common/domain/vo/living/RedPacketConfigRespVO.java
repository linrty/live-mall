package top.linrty.live.common.domain.vo.living;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 15:35
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
public class RedPacketConfigRespVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 5117539613836783248L;

    @Schema(description = "主播id")
    private Long anchorId;

    @Schema(description = "红包总金额")
    private Integer totalPrice;

    @Schema(description = "红包总数量")
    private Integer totalCount;

    @Schema(description = "红包配置码")
    private String configCode;

    @Schema(description = "备注")
    private String remark;

}
