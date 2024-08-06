package top.linrty.top.client.po;

import lombok.Data;

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
public class IMMsgBody implements Serializable {
    @Serial
    private static final long serialVersionUID = -7657602083071950966L;


    private int appId;

    private Long userId;

    private String token;

    private String data;

}
