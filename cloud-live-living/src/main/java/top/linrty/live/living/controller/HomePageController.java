package top.linrty.live.living.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.linrty.live.common.domain.vo.CommonRespVO;
import top.linrty.live.common.utils.RequestContext;
import top.linrty.live.common.domain.vo.living.HomePageVO;
import top.linrty.live.living.service.IHomePageService;

import static top.linrty.live.common.constants.HTTPKeyConstants.HTTP_HEADER_USER_ID;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/4 23:01
 * @Version: 1.0
 **/
@RestController
@RequestMapping("/home")
public class HomePageController {

    @Resource
    private IHomePageService homePageService;

    @PostMapping("/initPage")
    public CommonRespVO initPage() {
        String userIdStr = RequestContext.get(HTTP_HEADER_USER_ID).toString();
        Long userId = userIdStr== null? null :Long.parseLong(RequestContext.get(HTTP_HEADER_USER_ID).toString());
        HomePageVO homePageVO = homePageService.initPage(userId);
        if (userId != null) {
            homePageVO.setLoginStatus(true);
        }
        return CommonRespVO.success().setData(homePageVO);
    }
}
