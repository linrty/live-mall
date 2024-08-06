package top.linrty.live.living.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.linrty.live.common.domain.dto.living.GiftConfigDTO;
import top.linrty.live.living.domain.po.GiftConfig;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/6 22:02
 * @Version: 1.0
 **/
@Mapper
public interface GiftConfigMapper extends BaseMapper<GiftConfig> {
}
