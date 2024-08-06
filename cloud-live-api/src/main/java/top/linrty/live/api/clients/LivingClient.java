package top.linrty.live.api.clients;

import top.linrty.live.common.domain.dto.living.LivingRoomReqDTO;
import top.linrty.live.common.domain.dto.living.LivingRoomRespDTO;
import top.linrty.live.common.domain.vo.PageRespVO;

import java.util.List;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/4 23:24
 * @Version: 1.0
 **/
public interface LivingClient {

    /**
     * 根据用户id查询是否正在开播
     *
     * @param roomId
     * @return
     */
    LivingRoomRespDTO queryByRoomId(Integer roomId);

    List<Long> queryUserIdsByRoomId(LivingRoomReqDTO livingRoomReqDTO);

}
