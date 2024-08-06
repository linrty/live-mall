package top.linrty.live.common.domain.po.im;

import io.netty.util.ReferenceCounted;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.linrty.live.common.constants.im.IMConstants;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/31 16:52
 * @Version: 1.0
 **/
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class IMMsg implements Serializable, ReferenceCounted {

    private static final long serialVersionUID = -7007538930769644633L;

    // 魔数：用于做基本校验
    private short magic;

    // 用于记录body的长度
    private int len;

    // 用于标识当前消息的作用，后序交给不同的handler去处理
    private int code;

    // 存储消息体的内容，使用ByteBuf来管理
    private byte[] body;

    private final AtomicInteger refCnt = new AtomicInteger(1); // 管理引用计数

    public static IMMsg build(int code, String data) {
        IMMsg imMsg = new IMMsg();
        imMsg.setMagic(IMConstants.DEFAULT_MAGIC);
        imMsg.setCode(code);
        imMsg.setBody(data.getBytes());
        imMsg.setLen(imMsg.getBody().length);
        return imMsg;
    }

    @Override
    public int refCnt() {
        return refCnt.get();
    }

    @Override
    public ReferenceCounted retain() {
        refCnt.incrementAndGet();
        return this;
    }

    @Override
    public ReferenceCounted retain(int increment) {
        if (increment < 0) {
            throw new IllegalArgumentException("increment must be greater or equal to 0");
        }
        refCnt.addAndGet(increment);
        return this;
    }

    @Override
    public ReferenceCounted touch() {
        return this;
    }

    @Override
    public ReferenceCounted touch(Object hint) {
        return this;
    }

    @Override
    public boolean release() {
        return release(1);
    }

    @Override
    public boolean release(int decrement) {
        if (decrement < 0) {
            throw new IllegalArgumentException("decrement must be greater or equal to 0");
        }
        while (true) {
            int currentCnt = refCnt.get();
            if (currentCnt == 0) {
                return false;
            }
            int newCnt = currentCnt - decrement;
            if (newCnt < 0) {
                throw new IllegalStateException("decrement larger than current reference count");
            }
            if (refCnt.compareAndSet(currentCnt, newCnt)) {
                if (newCnt == 0) {
                    // 释放body中的ByteBuf资源
                    return true;
                }
                break;
            }
        }
        return false;
    }
}
