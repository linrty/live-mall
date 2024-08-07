package top.linrty.live.living.service;

import top.linrty.live.common.domain.dto.living.RedPacketConfigReqDTO;
import top.linrty.live.common.domain.dto.living.RedPacketReceiveDTO;
import top.linrty.live.living.domain.po.RedPacketConfig;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 15:38
 * @Version: 1.0
 **/
public interface IRedPacketConfigService {

    /**
     * 根据主播id查询有无发放红包雨的特权
     */
    RedPacketConfig queryByAnchorId(Long anchorId);

    /**
     * 根据code查询已准备的红包雨配置信息
     */
    RedPacketConfig queryByConfigCode(String code);

    /**
     * 新增红包雨配置
     */
    boolean addOne(RedPacketConfig redPacketConfig);

    /**
     * 更新红包雨配置
     */
    boolean updateById(RedPacketConfig redPacketConfig);

    /**
     * 主播开始准备红包雨
     */
    boolean prepareRedPacket(Long anchorId);

    /**
     * 直播间用户领取红包
     */
    RedPacketReceiveDTO receiveRedPacket(RedPacketConfigReqDTO redPacketConfigReqDTO);

    /**
     * 开始红包雨
     */
    Boolean startRedPacket(RedPacketConfigReqDTO reqDTO);

    /**
     * 接收到抢红包的消息过后，进行异步处理的handler
     */
    void receiveRedPacketHandler(RedPacketConfigReqDTO reqDTO, Integer price);
}
