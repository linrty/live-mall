package top.linrty.live.msg.handler;

import top.linrty.live.common.domain.po.im.IMMsgBody;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/3 23:21
 * @Version: 1.0
 **/
public interface MessageHandler {
    /**
     * 处理im发送过来的业务消息包
     */
    void onMsgReceive(IMMsgBody imMsgBody);
}
