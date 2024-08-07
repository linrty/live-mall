package top.linrty.live.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 将List拆分为小的List，用于Redis中list的存入，避免 Redis的输入输出缓冲区 和 网络 发生堵塞
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 16:01
 * @Version: 1.0
 **/
public class ListUtils {

    /**
     * 将一个大List集合拆分为多个子list集合（每个子集合有subNum个元素）
     */
    public static <T> List<List<T>> splistList(List<T> list, int subNum) {
        List<List<T>> resultList = new ArrayList<>();
        int priIndex = 0;
        int lastIndex = 0;
        int insertTime = list.size() / subNum;
        List<T> subList;
        for (int i = 0; i <= insertTime; i++) {
            priIndex = subNum * i;
            lastIndex = priIndex + subNum;
            if (i != insertTime) {
                subList = list.subList(priIndex, lastIndex);
            } else {
                subList = list.subList(priIndex, list.size());
            }
            if (subList.size() > 0) {
                resultList.add(subList);
            }
        }
        return resultList;
    }
}
