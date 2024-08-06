package top.linrty.live.living.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.linrty.live.common.domain.dto.living.GiftRecordDTO;
import top.linrty.live.common.utils.ConvertBeanUtils;
import top.linrty.live.living.domain.po.GiftRecord;
import top.linrty.live.living.mapper.GiftRecordMapper;
import top.linrty.live.living.service.IGiftRecordService;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/6 22:24
 * @Version: 1.0
 **/
@Service
@Slf4j
public class GiftRecordServiceImpl implements IGiftRecordService {

    @Resource
    private GiftRecordMapper giftRecordMapper;

    @Override
    public void insertOne(GiftRecordDTO giftRecordDTO) {
        GiftRecord giftRecord = ConvertBeanUtils.convert(giftRecordDTO, GiftRecord.class);
        giftRecordMapper.insert(giftRecord);
    }
}
