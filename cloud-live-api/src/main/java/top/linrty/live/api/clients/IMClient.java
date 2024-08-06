package top.linrty.live.api.clients;

import top.linrty.live.common.domain.po.im.IMMsgBody;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/4 13:07
 * @Version: 1.0
 **/
public interface IMClient {
    /**
     * 用于路由发送消息
     * @param imMsgBody
     */
    void sendMsg(IMMsgBody imMsgBody);

    /**
     * 在对应的业务场景下用户在线检测
     */
    boolean onlineCheck(Long userId, Integer appId);
}
