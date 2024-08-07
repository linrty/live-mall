package top.linrty.live.common.domain.vo.shop;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 16:52
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
public class ShopCarRespVO {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "房间id")
    private Integer roomId;

    @Schema(description = "购物车商品列表")
    private List<ShopCarItemRespVO> shopCarItemRespVOS;
}
