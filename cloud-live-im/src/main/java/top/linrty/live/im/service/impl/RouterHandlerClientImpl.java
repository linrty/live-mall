package top.linrty.live.im.service.impl;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import top.linrty.live.api.clients.RouterHandlerClient;
import top.linrty.live.common.domain.po.im.IMMsgBody;
import top.linrty.live.im.service.IRouterHandlerService;

import java.util.List;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/6 19:06
 * @Version: 1.0
 **/
@DubboService
public class RouterHandlerClientImpl implements RouterHandlerClient {


    @Resource
    private IRouterHandlerService routerHandlerService;

    @Override
    public void sendMsg(IMMsgBody imMsgBody) {
        routerHandlerService.onReceive(imMsgBody);
    }

    @Override
    public void batchSendMsg(List<IMMsgBody> imMsgBodyList) {
        imMsgBodyList.forEach(imMsgBody -> routerHandlerService.onReceive(imMsgBody));
    }
}
