package top.linrty.live.common.domain.vo.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 1:35
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
public class PayProductVO {
    /**
     * 当前余额
     */
    @Schema(description = "当前余额")
    private Integer currentBalance;
    /**
     * 一系列付费产品
     */
    @Schema(description = "一系列付费产品")
    private List<PayProductItemVO> payProductItemVOList;
}
