package top.linrty.live.common.domain.dto.shop;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 16:54
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
public class PrepareOrderDTO {

    private Long userId;

    private Integer roomId;
}
