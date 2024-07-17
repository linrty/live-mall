package top.linrty.live.api.clients;

import top.linrty.live.common.domain.dto.UserDTO;

/**
 * @Description: User模块内部调用
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/17 23:04
 * @Version: 1.0
 **/
public interface UserClient {

    /**
     * 根据用户id进行查询
     *
     * @param userId
     * @return
     */
    UserDTO getUserById(Long userId);

    /**
     * 更新用户信息
     * @param userDTO
     * @return
     */
    boolean updateUserInfo(UserDTO userDTO);

    /**
     * 插入用户
     * @param userDTO
     * @return
     */
    boolean insertOne(UserDTO userDTO);
}
