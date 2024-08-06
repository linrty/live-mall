package top.linrty.live.common.domain.po.im;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/1 11:43
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
public class IMMsgBody implements Serializable {
    @Serial
    private static final long serialVersionUID = -7657602083071950966L;

    private String msgId;

    private int appId;

    private Long userId;

    private String token;

    private String data;

    private int bizCode;

}
