package top.linrty.live.living.service;

import top.linrty.live.common.domain.vo.living.HomePageVO;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/4 23:01
 * @Version: 1.0
 **/
public interface IHomePageService {

    HomePageVO initPage(Long userId);
}
