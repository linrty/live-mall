package top.linrty.live.im.service;

import top.linrty.live.common.domain.po.im.IMMsgBody;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/3 15:46
 * @Version: 1.0
 **/
public interface IRouterHandlerService {

    /**
     * 当收到来自Router定向转发的业务服务的请求时，进行处理
     */
    void onReceive(IMMsgBody imMsgBody);

    boolean sendMsgToClient(IMMsgBody imMsgBody);
}
