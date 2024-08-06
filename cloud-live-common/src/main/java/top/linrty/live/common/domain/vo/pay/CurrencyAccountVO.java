package top.linrty.live.common.domain.vo.pay;

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
 * @Date: 2024/8/6 22:36
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
public class CurrencyAccountVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 4594540862190026761L;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "当前余额")
    private int currentBalance;

    @Schema(description = "累计充值")
    private int totalCharged;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;
}
