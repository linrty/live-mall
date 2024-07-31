package top.linrty.live.im.domain.po;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.linrty.live.common.constants.ImConstants;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/31 16:52
 * @Version: 1.0
 **/
@Data
@NoArgsConstructor
public class ImMsg implements Serializable {

    @Serial
    private static final long serialVersionUID = -7007538930769644633L;

    //魔数：用于做基本校验
    @Schema(description = "魔数")
    private short magic;

    //用于记录body的长度
    @Schema(description = "消息体长度")
    private int len;

    //用于标识当前消息的作用，后序交给不同的handler去处理
    @Schema(description = "消息类型")
    private int code;

    //存储消息体的内容，一般会按照字节数组的方式去存放
    @Schema(description = "消息体")
    private byte[] body;

    public static ImMsg build(int code, String data) {
        ImMsg imMsg = new ImMsg();
        imMsg.setMagic(ImConstants.DEFAULT_MAGIC);
        imMsg.setCode(code);
        imMsg.setBody(data.getBytes());
        imMsg.setLen(imMsg.getBody().length);
        return imMsg;
    }

}
