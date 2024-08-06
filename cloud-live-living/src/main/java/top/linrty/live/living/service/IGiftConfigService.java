package top.linrty.live.living.service;

import top.linrty.live.common.domain.dto.living.GiftConfigDTO;
import top.linrty.live.common.domain.vo.living.GiftConfigVO;

import java.util.List;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/6 22:00
 * @Version: 1.0
 **/
public interface IGiftConfigService {

    /**
     * 根据id查询礼物信息
     */
    GiftConfigVO getByGiftId(Integer giftId);

    /**
     * 查询所有礼物信息
     */
    List<GiftConfigVO> queryGiftList();

    /**
     * 插入一个礼物信息
     */
    void insertOne(GiftConfigDTO giftConfigDTO);

    /**
     * 更新礼物信息
     */
    void updateOne(GiftConfigDTO giftConfigDTO);
}
