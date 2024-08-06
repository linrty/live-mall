package top.linrty.live.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.linrty.live.pay.domain.po.CurrencyAccount;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/6 22:45
 * @Version: 1.0
 **/
@Mapper
public interface CurrencyAccountMapper extends BaseMapper<CurrencyAccount> {
    @Update("update t_currency_account set current_balance = current_balance + #{num} where user_id = #{userId}")
    void incr(@Param("userId") Long userId, @Param("num") int num);

    @Update("update t_currency_account set current_balance = current_balance - #{num} where user_id = #{userId}")
    void decr(@Param("userId") Long userId, @Param("num") int num);

    @Select("select current_balance from t_currency_account where user_id = #{userId} and status = 1")
    Integer queryBalance(Long userId);
}
