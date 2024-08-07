package top.linrty.live.pay.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import top.linrty.live.common.enums.StatusEnum;
import top.linrty.live.pay.domain.po.PayTopic;
import top.linrty.live.pay.mapper.PayTopicMapper;
import top.linrty.live.pay.service.IPayTopicService;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 11:34
 * @Version: 1.0
 **/
@Service
public class PayTopicServiceImpl implements IPayTopicService {

    @Resource
    private PayTopicMapper payTopicMapper;

    @Override
    @DS("read_db")
    public PayTopic getByCode(Integer code) {
        LambdaQueryWrapper<PayTopic> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PayTopic::getBizCode, code);
        queryWrapper.eq(PayTopic::getStatus, StatusEnum.VALID_STATUS.getCode());
        queryWrapper.last("limit 1");
        return payTopicMapper.selectOne(queryWrapper);
    }
}
