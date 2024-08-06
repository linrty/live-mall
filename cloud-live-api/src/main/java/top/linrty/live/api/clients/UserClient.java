package top.linrty.live.api.clients;

import top.linrty.live.common.domain.dto.user.MsgCheckDTO;
import top.linrty.live.common.domain.dto.user.UserDTO;
import top.linrty.live.common.enums.im.MsgSendResultEnum;
import top.linrty.live.common.enums.user.UserTagsEnum;

import java.util.List;
import java.util.Map;

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
     * 设置标签
     *
     * @param userId 用户id
     * @param userTagsEnum 标签枚举
     * @return boolean
     */
    boolean setTag(Long userId, UserTagsEnum userTagsEnum);

    /**
     * 取消标签
     *
     * @param userId 用户id
     * @param userTagsEnum 标签枚举
     * @return boolean
     */
    boolean cancelTag(Long userId,UserTagsEnum userTagsEnum);

    /**
     * 是否包含某个标签
     *
     * @param userId 用户id
     * @param userTagsEnum 标签枚举
     * @return boolean
     */
    boolean containTag(Long userId,UserTagsEnum userTagsEnum);

    /**
     * 发送短信接口
     *
     * @param phone
     * @return
     */
    MsgSendResultEnum sendLoginCode(String phone);

    /**
     * 校验登录验证码
     *
     * @param phone
     * @param code
     * @return
     */
    MsgCheckDTO checkLoginCode(String phone, Integer code);


    Map<Long, UserDTO> batchQueryUserInfo(List<Long> userIds);
}
