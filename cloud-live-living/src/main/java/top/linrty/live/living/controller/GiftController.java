package top.linrty.live.living.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.linrty.live.common.domain.dto.living.GiftReqDTO;
import top.linrty.live.common.domain.vo.CommonRespVO;
import top.linrty.live.common.domain.vo.living.GiftConfigVO;
import top.linrty.live.living.service.IGiftConfigService;
import top.linrty.live.living.service.IGiftRecordService;
import top.linrty.live.living.service.IGiftService;

import java.util.List;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/6 22:07
 * @Version: 1.0
 **/
@RestController
@RequestMapping("/gift")
@Schema(description = "礼物API")
public class GiftController {

    private IGiftConfigService giftConfigService;

    private IGiftRecordService giftRecordService;

    private IGiftService giftService;

    @PostMapping("/listGift")
    public CommonRespVO listGift() {
        List<GiftConfigVO> giftConfigVOS = giftConfigService.queryGiftList();
        return CommonRespVO.success().setData(giftConfigVOS);
    }

    @PostMapping("/send")
    public CommonRespVO send(GiftReqDTO giftReqDTO) {
        // TODO 礼物发送
        return CommonRespVO.success().setData(giftService.send(giftReqDTO));
    }
}
