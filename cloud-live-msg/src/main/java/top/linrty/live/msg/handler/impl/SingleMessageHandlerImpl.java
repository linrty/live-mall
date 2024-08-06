package top.linrty.live.msg.handler.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import top.linrty.live.api.clients.IMClient;
import top.linrty.live.api.clients.LivingClient;
import top.linrty.live.api.clients.RouterClient;
import top.linrty.live.common.domain.dto.im.MsgDTO;
import top.linrty.live.common.domain.dto.living.LivingRoomReqDTO;
import top.linrty.live.common.domain.po.im.IMMsgBody;
import top.linrty.live.common.enums.BizEnum;
import top.linrty.live.common.enums.im.IMMsgBizCodeEnum;
import top.linrty.live.common.enums.im.IMMsgCodeEnum;
import top.linrty.live.msg.handler.MessageHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/3 23:26
 * @Version: 1.0
 **/
@Component
@Slf4j
public class SingleMessageHandlerImpl implements MessageHandler {

    @DubboReference
    private RouterClient routerClient;

    @DubboReference
    private LivingClient livingClient;
    @Override
    public void onMsgReceive(IMMsgBody imMsgBody) {
        int bizCode = imMsgBody.getAppId();
        // 直播间的聊天消息
        if (bizCode == IMMsgCodeEnum.IM_LIVING_ROOM_MSG.getCode()) {
            MsgDTO messageDTO = JSON.parseObject(imMsgBody.getData(), MsgDTO.class);
            Integer roomId = messageDTO.getRoomId();
            LivingRoomReqDTO reqDTO = new LivingRoomReqDTO();
            reqDTO.setRoomId(roomId);
            reqDTO.setAppId(imMsgBody.getAppId());
            //自己不用发
            List<Long> userIdList = livingClient.queryUserIdsByRoomId(reqDTO).stream().filter(x -> !x.equals(imMsgBody.getUserId())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(userIdList)){
                log.warn("[SingleMessageHandlerImpl] 要转发的userIdList为空");
                return;
            }
            List<IMMsgBody> respMsgBodyList = new ArrayList<>();
            userIdList.forEach(userId -> {
                IMMsgBody respMsgBody = new IMMsgBody()
                        .setAppId(BizEnum.LIVE_BIZ.getCode())
                        .setBizCode(IMMsgBizCodeEnum.LIVING_ROOM_IM_CHAT_MSG_BIZ.getCode())
                        .setData(JSON.toJSONString(messageDTO))
                        .setUserId(userId);
                respMsgBodyList.add(respMsgBody);
            });
            //将消息推送给router进行转发给im服务器
            routerClient.batchSendMsg(respMsgBodyList);
        }
    }
}
