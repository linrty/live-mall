package top.linrty.live.router.service;

import top.linrty.live.common.domain.po.im.IMMsgBody;

import java.util.List;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/3 16:29
 * @Version: 1.0
 **/
public interface IRouterService {
    boolean sendMsg(IMMsgBody imMsgBody);

    void batchSendMsg(List<IMMsgBody> imMsgBodyList);
}
