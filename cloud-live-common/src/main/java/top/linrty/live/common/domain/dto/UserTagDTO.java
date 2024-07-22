package top.linrty.live.common.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/22 17:59
 * @Version: 1.0
 **/
public class UserTagDTO implements Serializable {

    private static final long serialVersionUID = 1662020070373016278L;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "标签信息01")
    private Long tagInfo01;

    @Schema(description = "标签信息02")
    private Long tagInfo02;

    @Schema(description = "标签信息03")
    private Long tagInfo03;
}
