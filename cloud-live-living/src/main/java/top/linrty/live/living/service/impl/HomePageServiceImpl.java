package top.linrty.live.living.service.impl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import top.linrty.live.api.clients.UserClient;
import top.linrty.live.common.domain.dto.user.UserDTO;
import top.linrty.live.common.enums.user.UserTagsEnum;
import top.linrty.live.common.domain.vo.living.HomePageVO;
import top.linrty.live.living.service.IHomePageService;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/4 23:01
 * @Version: 1.0
 **/
@Service
public class HomePageServiceImpl implements IHomePageService {

    @DubboReference
    private UserClient userClient;

    @Override
    public HomePageVO initPage(Long userId) {
        UserDTO userDTO = userClient.getUserById(userId);
        HomePageVO homePageVO = new HomePageVO();
        homePageVO.setLoginStatus(false);
        if (userId != null){
            // VIP用户才可以开播
            homePageVO.setAvatar(userDTO.getAvatar())
                    .setUserId(userId)
                    .setNickName(userDTO.getNickName())
                    .setShowStartLivingBtn(userClient.containTag(userId, UserTagsEnum.IS_VIP));
        }
        return homePageVO;
    }
}
