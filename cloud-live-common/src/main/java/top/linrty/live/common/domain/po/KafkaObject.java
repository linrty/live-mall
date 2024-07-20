package top.linrty.live.common.domain.po;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/20 23:33
 * @Version: 1.0
 **/
@Data
@AllArgsConstructor
public class KafkaObject {

    @Schema(description = "状态码")
    private String code;

    @Schema(description = "用户id")
    private String userId;
}
