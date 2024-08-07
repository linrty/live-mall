package top.linrty.live.living.service;

import top.linrty.live.common.domain.dto.PageReqDTO;
import top.linrty.live.common.domain.dto.im.IMOfflineDTO;
import top.linrty.live.common.domain.dto.im.IMOnlineDTO;
import top.linrty.live.common.domain.dto.living.LivingRoomReqDTO;
import top.linrty.live.common.domain.dto.living.LivingRoomRespDTO;
import top.linrty.live.common.domain.dto.living.OnlinePKReqDTO;
import top.linrty.live.common.domain.vo.PageRespVO;
import top.linrty.live.common.domain.vo.living.LivingRoomInitVO;
import top.linrty.live.common.domain.vo.living.LivingRoomRespVO;

import java.util.List;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/4 23:37
 * @Version: 1.0
 **/
public interface ILivingRoomService {
    /**
     * 开启直播间
     *
     * @param type
     * @return
     */
    Integer startingLiving(Integer type);

    /**
     * 关闭直播间
     *
     * @param roomId
     * @return
     */
    boolean closeLiving(Integer roomId);

    /**
     * 根据用户id查询是否正在开播
     *
     * @param roomId
     * @return
     */
    LivingRoomRespDTO queryByRoomId(Integer roomId);

    /**
     * 验证当前用户是否是主播身份
     */
    LivingRoomInitVO anchorConfig(Long userId, Integer roomId);

    /**
     * 查询直播间列表（分页）
     */
    PageRespVO<LivingRoomRespVO> list(PageReqDTO<LivingRoomReqDTO> pageReqDTO);


    List<LivingRoomRespVO> listAllLivingRoomFromDB(Integer type);

    /**
     * 用户登录在线roomId与userId关联处理
     */
    void userOnlineHandler(IMOnlineDTO imOnlineDTO);
    /**
     * 用户离线roomId与userId关联处理
     */
    void userOfflineHandler(IMOfflineDTO imOfflineDTO);
    /**
     * 支持根据roomId查询出批量的userId（set）存储，3000个人，元素非常多，O(n)
     */
    List<Long> queryUserIdsByRoomId(LivingRoomReqDTO livingRoomReqDTO);
    /**
     * 当PK直播间连上线准备PK时，调用该请求
     */
    boolean onlinePK(OnlinePKReqDTO onlinePKReqDTO);

    /**
     * 用户在pk直播间下线
     */
    boolean offlinePk(LivingRoomReqDTO livingRoomReqDTO);

    /**
     * 根据roomId查询当前pk人是谁
     *
     * @param roomId
     * @return
     */
    Long queryOnlinePkUserId(Integer roomId);

    LivingRoomRespDTO queryByAnchorId(Long anchorId);
}
