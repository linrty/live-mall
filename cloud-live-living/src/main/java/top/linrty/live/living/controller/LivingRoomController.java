package top.linrty.live.living.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.linrty.live.common.anno.RequestLimit;
import top.linrty.live.common.constants.HTTPKeyConstants;
import top.linrty.live.common.domain.dto.PageReqDTO;
import top.linrty.live.common.domain.dto.living.*;
import top.linrty.live.common.domain.vo.CommonRespVO;
import top.linrty.live.common.domain.vo.living.RedPacketReceiveVO;
import top.linrty.live.common.exception.ParamException;
import top.linrty.live.common.exception.UnknownException;
import top.linrty.live.common.utils.RequestContext;
import top.linrty.live.common.domain.vo.living.LivingRoomInitVO;
import top.linrty.live.living.service.ILivingRoomService;
import top.linrty.live.living.service.IRedPacketConfigService;

import static top.linrty.live.common.constants.HTTPKeyConstants.HTTP_HEADER_USER_ID;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/4 23:59
 * @Version: 1.0
 **/
@RestController
@RequestMapping("/living")
public class LivingRoomController {
    @Resource
    private ILivingRoomService livingRoomService;

    @Resource
    private IRedPacketConfigService redPacketConfigService;

    @PostMapping("/startingLiving")
    @RequestLimit(limit = 1, second = 10, msg = "开播请求过于频繁，请稍后再试")
    public CommonRespVO startingLiving(Integer type) {
        if (type == null) {
            throw new ParamException("需要给定直播间类型");
        }
        Integer roomId = livingRoomService.startingLiving(type);
        LivingRoomInitVO livingRoomInitVO = new LivingRoomInitVO();
        livingRoomInitVO.setRoomId(roomId);
        return CommonRespVO.success().setData(livingRoomInitVO);
    }

    @PostMapping("/closeLiving")
    @RequestLimit(limit = 1, second = 10, msg = "关播请求过于频繁，请稍后再试")
    public CommonRespVO closeLiving(Integer roomId) {
        if (roomId == null) {
            throw new ParamException("需要给定直播间id");
        }
        boolean status = livingRoomService.closeLiving(roomId);
        if (!status) {
            throw new UnknownException("关播异常，请稍后再试");
        }
        return CommonRespVO.success();
    }

    @PostMapping("/anchorConfig")
    public CommonRespVO anchorConfig(Integer roomId) {
        String userIdStr = RequestContext.get(HTTP_HEADER_USER_ID).toString();
        Long userId = Long.parseLong(userIdStr);
        return CommonRespVO.success().setData(livingRoomService.anchorConfig(userId, roomId));
    }

    @PostMapping("/list")
    public CommonRespVO list(PageReqDTO<LivingRoomReqDTO> livingRoomReqDTO) {
        if (livingRoomReqDTO == null || livingRoomReqDTO.getData() == null || livingRoomReqDTO.getData().getType() == null) {
            throw new ParamException("需要给定直播间类型");
        }
        if (livingRoomReqDTO.getPage() <= 0 || livingRoomReqDTO.getPageSize() > 100) {
            throw new ParamException("分页查询参数错误");
        }
        return CommonRespVO.success().setData(livingRoomService.list(livingRoomReqDTO));
    }

    @PostMapping("/onlinePK")
    @RequestLimit(limit = 1, second = 3)
    public CommonRespVO onlinePk(OnlinePKReqDTO onlinePKReqDTO) {
        if (onlinePKReqDTO == null) {
            throw new ParamException("需要给定直播间ID");
        }
        return CommonRespVO.success().setData(livingRoomService.onlinePK(onlinePKReqDTO));
    }

    @RequestLimit(limit = 1, second = 10, msg = "正在初始化红包数据，请稍等")
    @PostMapping("/prepareRedPacket")
    public CommonRespVO prepareRedPacket(LivingRoomReqDTO livingRoomReqDTO) {
        LivingRoomRespDTO livingRoomRespDTO = livingRoomService.queryByRoomId(livingRoomReqDTO.getRoomId());
        if (livingRoomRespDTO == null){
            throw new ParamException("直播间不存在");
        }
        Long userId = Long.parseLong(RequestContext.get(HTTP_HEADER_USER_ID).toString());
        if (!livingRoomReqDTO.getAnchorId().equals(userId)){
            throw new ParamException("只有主播才能初始化红包");
        }
        return CommonRespVO.success().setData(redPacketConfigService.prepareRedPacket(userId));
    }

    @RequestLimit(limit = 1, second = 10, msg = "正在广播直播间用户，请稍等")
    @PostMapping("/startRedPacket")
    public CommonRespVO startRedPacket(LivingRoomReqDTO livingRoomReqDTO) {
        Long userId = Long.parseLong(RequestContext.get(HTTP_HEADER_USER_ID).toString());
        LivingRoomRespDTO livingRoomRespDTO = livingRoomService.queryByAnchorId(userId);
        if (livingRoomRespDTO == null){
            throw new UnknownException("直播间不存在");
        }
        RedPacketConfigReqDTO redPacketConfigReqDTO = new RedPacketConfigReqDTO();
        redPacketConfigReqDTO.setUserId(userId)
                .setRedPacketConfigCode(livingRoomReqDTO.getRedPacketConfigCode())
                .setRoomId(livingRoomRespDTO.getId());
        return CommonRespVO.success().setData(redPacketConfigService.startRedPacket(redPacketConfigReqDTO));
    }

    @RequestLimit(limit = 1, second = 7, msg = "")
    @PostMapping("/getRedPacket")
    public CommonRespVO getRedPacket(LivingRoomReqDTO livingRoomReqDTO) {
        Long userId = Long.parseLong(RequestContext.get(HTTP_HEADER_USER_ID).toString());
        RedPacketConfigReqDTO reqDTO = new RedPacketConfigReqDTO();
        reqDTO.setUserId(userId)
                .setRedPacketConfigCode(livingRoomReqDTO.getRedPacketConfigCode());
        RedPacketReceiveDTO redPacketReceiveDTO = redPacketConfigService.receiveRedPacket(reqDTO);
        RedPacketReceiveVO resVO = new RedPacketReceiveVO();
        if(redPacketReceiveDTO == null){
            resVO.setMsg("红包已领完");
        } else{
            resVO.setMsg(redPacketReceiveDTO.getNotifyMsg())
                    .setPrice(redPacketReceiveDTO.getPrice());
        }
        return CommonRespVO.success().setData(resVO);
    }
}
