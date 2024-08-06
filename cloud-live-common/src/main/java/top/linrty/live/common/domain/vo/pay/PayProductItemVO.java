package top.linrty.live.common.domain.vo.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 1:35
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
public class PayProductItemVO {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "价格")
    private Integer coinNum;
}
