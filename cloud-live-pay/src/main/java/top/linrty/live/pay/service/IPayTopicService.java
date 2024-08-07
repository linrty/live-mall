package top.linrty.live.pay.service;

import top.linrty.live.pay.domain.po.PayTopic;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 11:33
 * @Version: 1.0
 **/
public interface IPayTopicService {

    PayTopic getByCode(Integer code);
}
