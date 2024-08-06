package top.linrty.live.living.service;

import top.linrty.live.common.domain.dto.living.GiftRecordDTO;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/6 22:01
 * @Version: 1.0
 **/
public interface IGiftRecordService {

    /**
     * 插入一条送礼记录
     */
    void insertOne(GiftRecordDTO giftRecordDTO);
}
