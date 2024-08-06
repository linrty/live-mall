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
 * @Date: 2024/8/6 21:57
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
public class GiftRecordDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -3007370091488635874L;

    @Schema(description = "礼物记录id")
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "对象id")
    private Long objectId;

    @Schema(description = "礼物来源")
    private Integer source;

    @Schema(description = "礼物价格")
    private Integer price;

    @Schema(description = "礼物单位")
    private Integer priceUnit;

    @Schema(description = "礼物id")
    private Integer giftId;

    @Schema(description = "礼物发送时间")
    private Date sendTime;
}
