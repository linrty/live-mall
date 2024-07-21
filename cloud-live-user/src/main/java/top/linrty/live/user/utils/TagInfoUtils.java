package top.linrty.live.user.utils;

/**
 * @Description: UserTag用户标签工具类
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/21 23:11
 * @Version: 1.0
 **/
public class TagInfoUtils {
    /**
     * 判断在tagInfo的二进制位中是否存在要匹配的标matchTag
     * @param tagInfo 数据库中存储的tag值
     * @param matchTag 要匹配是否存在该标签
     * @return 是否存在
     */
    public static boolean isContain(Long tagInfo, Long matchTag) {
        return tagInfo != null && matchTag != null && matchTag != 0 && (tagInfo & matchTag) == matchTag;
    }
}
