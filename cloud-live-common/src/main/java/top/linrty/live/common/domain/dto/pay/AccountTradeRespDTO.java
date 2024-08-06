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
 * @Date: 2024/8/6 23:27
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
public class AccountTradeRespDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 7388807009440301192L;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "交易状态")
    private boolean isSuccess;
    //失败原因代码编号
    @Schema(description = "失败原因代码编号")
    private int code;

    @Schema(description = "失败原因")
    private String msg;
}
