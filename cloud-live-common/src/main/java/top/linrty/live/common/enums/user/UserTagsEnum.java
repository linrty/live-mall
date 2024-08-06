package top.linrty.live.common.enums.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @Description: 用户标签枚举
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/21 23:07
 * @Version: 1.0
 **/
@Getter
@AllArgsConstructor
public enum UserTagsEnum {
    IS_RICH((long) Math.pow(2, 0), "是否是有钱用户", "tag_info_01"),

    IS_VIP((long) Math.pow(2, 1), "是否是VIP用户", "tag_info_01"),

    IS_OLD_USER((long) Math.pow(2, 2), "是否是老用户", "tag_info_01"),
    ;

    private final long tag;

    private final String desc;

    private final String fieldName;
}
