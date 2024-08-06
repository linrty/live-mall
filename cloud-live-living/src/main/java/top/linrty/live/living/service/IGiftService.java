package top.linrty.live.living.service;

import top.linrty.live.common.domain.dto.living.GiftReqDTO;
import top.linrty.live.common.domain.vo.living.GiftConfigVO;

import java.util.List;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 0:30
 * @Version: 1.0
 **/
public interface IGiftService {

    boolean send(GiftReqDTO giftReqDTO);
}
