package top.linrty.live.router.service.impl;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import top.linrty.live.api.clients.RouterClient;
import top.linrty.live.common.domain.po.im.IMMsgBody;
import top.linrty.live.router.service.IRouterService;

import java.util.List;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/3 16:32
 * @Version: 1.0
 **/
@DubboService
public class RouterClientImpl implements RouterClient {

    @Resource
    private IRouterService routerService;

    @Override
    public boolean sendMsg(IMMsgBody imMsgBody) {
        routerService.sendMsg(imMsgBody);
        return true;
    }

    @Override
    public void batchSendMsg(List<IMMsgBody> imMsgBodyList) {
        routerService.batchSendMsg(imMsgBodyList);
    }
}
