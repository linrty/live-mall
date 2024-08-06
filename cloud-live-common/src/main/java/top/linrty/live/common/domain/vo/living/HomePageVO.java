package top.linrty.live.common.domain.vo.living;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/4 23:00
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
public class HomePageVO {

    @Schema(description = "登录状态")
    private boolean loginStatus;

    @Schema(description = "用户id")
    private long userId;

    @Schema(description = "用户昵称")
    private String nickName;

    @Schema(description = "用户头像")
    private String avatar;

    //是否是主播身份
    @Schema(description = "是否是主播身份")
    private boolean showStartLivingBtn;
}
