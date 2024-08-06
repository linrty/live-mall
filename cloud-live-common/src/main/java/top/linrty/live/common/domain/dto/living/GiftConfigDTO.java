package top.linrty.live.common.domain.dto.living;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/6 21:56
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
public class GiftConfigDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -1394363192115983898L;

    @Schema(description = "礼物id")
    private Integer giftId;

    @Schema(description = "礼物价格")
    private Integer price;

    @Schema(description = "礼物名称")
    private String giftName;

    @Schema(description = "礼物状态")
    private Integer status;

    @Schema(description = "礼物封面图")
    private String coverImgUrl;

    @Schema(description = "礼物svga图")
    private String svgaUrl;

    @Schema(description = "礼物创建时间")
    private Date createTime;

    @Schema(description = "礼物更新时间")
    private Date updateTime;
}
