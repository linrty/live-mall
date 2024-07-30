package top.linrty.live.common.domain.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/30 17:54
 * @Version: 1.0
 **/
@Data
public class UserLoginDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4290788036479984698L;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "token")
    private String token;

    @Schema(description = "登录状态")
    private boolean loginStatus;

    @Schema(description = "登录状态描述")
    private String desc;

}
