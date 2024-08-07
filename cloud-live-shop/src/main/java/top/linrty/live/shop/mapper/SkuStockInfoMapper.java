package top.linrty.live.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.linrty.live.shop.domain.po.SkuStockInfo;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 17:12
 * @Version: 1.0
 **/
@Mapper
public interface SkuStockInfoMapper extends BaseMapper<SkuStockInfo> {

    @Update("update t_sku_stock_info set stock_num = stock_num - #{num} where sku_id = #{skuId} and stock_num >= #{num}")
    boolean decrStockNumBySkuId(@Param("skuId") Long skuId, @Param("num") Integer num);
}
