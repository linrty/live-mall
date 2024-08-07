package top.linrty.live.living.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.linrty.live.living.domain.po.RedPacketConfig;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 15:39
 * @Version: 1.0
 **/
@Mapper
public interface RedPacketConfigMapper extends BaseMapper<RedPacketConfig> {

    @Update("update t_red_packet_config set total_get_price = total_get_price + #{price} where config_code = #{code}")
    void incrTotalGetPrice(@Param("code") String code, @Param("price") Integer price);

    @Update("update t_red_packet_config set total_get = total_get + 1 where config_code = #{code}")
    void incrTotalGetCount(String code);
}
