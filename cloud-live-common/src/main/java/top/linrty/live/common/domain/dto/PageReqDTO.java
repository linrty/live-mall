package top.linrty.live.common.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/5 17:14
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
public class PageReqDTO <T>{

    private T data;

    private Integer page;

    private Integer pageSize;
}
