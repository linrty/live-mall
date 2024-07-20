package top.linrty.live.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.linrty.live.user.domain.po.User;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/20 17:36
 * @Version: 1.0
 **/
@Mapper
public interface IUserMapper extends BaseMapper<User> {

}
