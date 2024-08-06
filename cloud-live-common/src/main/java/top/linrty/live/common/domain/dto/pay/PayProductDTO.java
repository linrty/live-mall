package top.linrty.live.common.domain.dto.pay;

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
 * @Date: 2024/8/7 1:20
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
public class PayProductDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 3297046546532825538L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "价格")
    private Integer price;

    @Schema(description = "额外信息")
    private String extra;

    @Schema(description = "类型")
    private Integer type;

    @Schema(description = "状态")
    private Integer validStatus;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;
}
