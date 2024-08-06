package top.linrty.live.common.constants.im;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/4 17:27
 * @Version: 1.0
 **/
public class IMMsgTopicNames {
    /**
     * 接收im系统发送的业务消息包
     */
    public static final String IM_BIZ_MSG_TOPIC = "im_biz_msg_topic";

    /**
     * 发送ack延迟消息的topic
     */
    public static final String IM_ACK_MSG_TOPIC = "im-ack-msg-topic";

    /**
     * 用户初次登录im服务发mq
     */
    public static final String IM_ONLINE_TOPIC = "im-online-topic";

    /**
     * 用户断开im服务发mq
     */
    public static final String IM_OFFLINE_TOPIC = "im-offline-topic";
}
