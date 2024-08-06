package top.linrty.live.common.domain.vo.pay;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 2:08
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
public class PayProductRespVO {
    private String orderId;
}
