package top.linrty.live.living.service.impl;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import top.linrty.live.api.clients.LivingClient;
import top.linrty.live.common.domain.dto.living.LivingRoomReqDTO;
import top.linrty.live.common.domain.dto.living.LivingRoomRespDTO;
import top.linrty.live.common.domain.vo.PageRespVO;
import top.linrty.live.living.service.ILivingRoomService;

import java.util.List;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/4 23:36
 * @Version: 1.0
 **/
@DubboService
public class LivingClientImpl implements LivingClient {

    @Resource
    private ILivingRoomService livingRoomService;

    @Override
    public LivingRoomRespDTO queryByRoomId(Integer roomId) {
        return livingRoomService.queryByRoomId(roomId);
    }

    @Override
    public List<Long> queryUserIdsByRoomId(LivingRoomReqDTO livingRoomReqDTO) {
        return livingRoomService.queryUserIdsByRoomId(livingRoomReqDTO);
    }

}
