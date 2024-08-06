package top.linrty.live.living.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.linrty.live.common.domain.dto.PageReqDTO;
import top.linrty.live.common.domain.dto.living.LivingRoomReqDTO;
import top.linrty.live.common.domain.vo.CommonRespVO;
import top.linrty.live.common.exception.ParamException;
import top.linrty.live.common.exception.UnknownException;
import top.linrty.live.common.utils.RequestContext;
import top.linrty.live.common.domain.vo.living.LivingRoomInitVO;
import top.linrty.live.living.service.ILivingRoomService;

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

    @PostMapping("/startingLiving")
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
}
