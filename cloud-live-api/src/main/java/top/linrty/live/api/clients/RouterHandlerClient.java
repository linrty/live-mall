package top.linrty.live.api.clients;

import top.linrty.live.common.domain.po.im.IMMsgBody;

import java.util.List;

/**
 * @Description: 路由消息处理
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/3 15:42
 * @Version: 1.0
 **/
public interface RouterHandlerClient {
    public void sendMsg(IMMsgBody imMsgBody);

    /**
     * 实现在直播间内进行消息的批量推送
     */
    void batchSendMsg(List<IMMsgBody> imMsgBodyList);
}
