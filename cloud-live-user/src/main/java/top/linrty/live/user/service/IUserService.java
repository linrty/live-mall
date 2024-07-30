package top.linrty.live.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.linrty.live.common.domain.dto.user.UserDTO;
import top.linrty.live.common.domain.dto.user.UserLoginVO;
import top.linrty.live.user.domain.po.User;

import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/20 17:37
 * @Version: 1.0
 **/
public interface IUserService extends IService<User> {

    /**
     * 发送登录验证码
     *
     * @param phone
     * @return
     */
    void sendLoginCode(String phone);

    UserLoginVO login(String phone, Integer code);
    /**
     * 根据用户id进行查询
     * @param userId 用户id
     * @return UserDTO
     */
    UserDTO getUserById(Long userId);

    /**
     * 更新用户信息
     * @param userDTO 用户信息
     * @return boolean
     */
    boolean updateUserInfo(UserDTO userDTO);

    /**
     * 插入用户
     * @param userDTO 用户信息
     * @return boolean
     */
    boolean insertOne(UserDTO userDTO);

    /**
     * 批量查询用户信息
     * @param userIdList 用户ID列表
     * @return Map<Long, UserDTO>
     */
    Map<Long, UserDTO> batchQueryUserInfo(List<Long> userIdList);
}
