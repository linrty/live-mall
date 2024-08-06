package top.linrty.live.im.service;

import top.linrty.live.common.domain.po.im.IMMsgBody;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/4 19:19
 * @Version: 1.0
 **/
public interface IAckMsgService {
    /**
     * 主要是客户端发送ack包给到服务端后，调用进行ack记录的移除
     */
    void doMsgAck(IMMsgBody imMsgBody);

    /**
     * 往Redis中记录下还未收到的消息的ack和已经重试的次数times
     */
    void recordMsgAck(IMMsgBody imMsgBody, int times);

    /**
     * 发送延迟消息，用于进行消息重试功能
     */
    void sendDelayMsg(IMMsgBody imMsgBody);

    /**
     * 获取ack消息的重试次数
     */
    int getMsgAckTimes(String msgId, Long userId, int appId);
}
