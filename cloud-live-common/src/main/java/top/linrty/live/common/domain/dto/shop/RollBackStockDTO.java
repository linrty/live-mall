package top.linrty.live.common.domain.dto.shop;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 17:00
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
public class RollBackStockDTO {

    private Long userId;

    private Long orderId;
}
