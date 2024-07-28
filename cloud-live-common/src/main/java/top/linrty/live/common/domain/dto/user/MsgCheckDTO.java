package top.linrty.live.common.domain.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Description: 记录短信相关信息
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/28 21:00
 * @Version: 1.0
 **/
@Data
@AllArgsConstructor
public class MsgCheckDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 2383695129958767484L;

    @Schema(description = "验证状态")
    private boolean checkStatus;

    @Schema(description = "描述")
    private String desc;
}
