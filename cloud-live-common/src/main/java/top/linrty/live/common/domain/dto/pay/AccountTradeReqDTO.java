package top.linrty.live.common.domain.dto.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/6 23:29
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
public class AccountTradeReqDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -3852469488748386015L;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "交易金额")
    private int num;
}
