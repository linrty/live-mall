package top.linrty.live.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.linrty.live.user.domain.po.Sms;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/28 21:37
 * @Version: 1.0
 **/
@Mapper
public interface ISmsMapper extends BaseMapper<Sms> {
}
