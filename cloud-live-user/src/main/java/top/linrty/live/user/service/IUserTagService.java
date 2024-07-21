package top.linrty.live.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.linrty.live.common.enums.UserTagsEnum;
import top.linrty.live.user.domain.po.UserTag;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/21 23:18
 * @Version: 1.0
 **/
public interface IUserTagService extends IService<UserTag> {
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
    boolean cancelTag(Long userId, UserTagsEnum userTagsEnum);

    /**
     * 是否包含某个标签
     *
     * @param userId 用户id
     * @param userTagsEnum 标签枚举
     * @return boolean
     */
    boolean containTag(Long userId,UserTagsEnum userTagsEnum);
}
