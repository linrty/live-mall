package top.linrty.live.user.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.linrty.live.common.config.redis.RedisKeyTime;
import top.linrty.live.common.domain.dto.user.MsgCheckDTO;
import top.linrty.live.common.enums.im.MsgSendResultEnum;
import top.linrty.live.common.utils.ThreadPoolManager;
import top.linrty.live.user.service.ISmsService;
import top.linrty.live.user.utils.MsgProviderCacheKeyBuilder;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/28 21:39
 * @Version: 1.0
 **/
@Service
@Slf4j
public class SmsService implements ISmsService {

    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

    @Resource
    private MsgProviderCacheKeyBuilder msgProviderCacheKeyBuilder;

    @Override
    public MsgSendResultEnum sendLoginCode(String phone) {
        if (StrUtil.isEmpty(phone)) {
            return MsgSendResultEnum.MSG_PARAM_ERROR;
        }
        // 生成6为验证码，有效期60s，同一个手机号不能重复发，Redis去存储验证码
        String key = msgProviderCacheKeyBuilder.buildSmsLoginCodeKey(phone);
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            log.warn("该手机号短信发送过于频繁，phone is {}", phone);
            return MsgSendResultEnum.SEND_FAIL;
        }
        int code = RandomUtil.randomInt(1000, 9999);
        redisTemplate.opsForValue().set(key, code, RedisKeyTime.EXPIRE_TIME_ONE_MINUTE, TimeUnit.SECONDS);
        // 发送验证码(模拟实现)
        ThreadPoolManager.commonAsyncPool.execute(() -> {
            this.sendSmsToCCP(phone, code);
        });
        return MsgSendResultEnum.SEND_SUCCESS;
    }

    @Override
    public MsgCheckDTO checkLoginCode(String phone, Integer code) {
        // 参数校验
        if (StrUtil.isEmpty(phone) || code == null || code < 1000) {
            return new MsgCheckDTO(false, "参数异常");
        }
        // redis校验验证码
        String key = msgProviderCacheKeyBuilder.buildSmsLoginCodeKey(phone);
        Integer cacheCode = redisTemplate.opsForValue().get(key);
        if (cacheCode == null || cacheCode < 1000) {
            return new MsgCheckDTO(false, "验证码已过期");
        }
        if (cacheCode.equals(code)) {
            redisTemplate.delete(key);
            return new MsgCheckDTO(true, "验证码校验成功");
        }
        return new MsgCheckDTO(false, "验证码校验失败");
    }

    /**
     * 通过容联云平台发送短信，可以将账号配置信息抽取到Nacos配置中心
     * @param phone
     * @param code
     * @return
     */
    private boolean sendSmsToCCP(String phone, Integer code) {
        try {
            // 生产环境请求地址：app.cloopen.com
            String serverIp = "app.cloopen.com";
            // 请求端口
            String serverPort = "8883";
            // 主账号,登陆云通讯网站后,可在控制台首页看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN
            String accountSId = "2c94811c9035ff9f0190f95b814428e4";
            String accountToken = "f2a725ae5c764d56924ccf61ab4af87d";
            // 请使用管理控制台中已创建应用的APPID
            String appId = "2c94811c9035ff9f0190f95b82d728eb";
            CCPRestSmsSDK sdk = new CCPRestSmsSDK();
            sdk.init(serverIp, serverPort);
            sdk.setAccount(accountSId, accountToken);
            sdk.setAppId(appId);
            sdk.setBodyType(BodyType.Type_JSON);
            String to = phone;
            String templateId = "1";
            // 测试开发短信模板：【云通讯】您的验证码是{1}，请于{2}分钟内正确输入。其中{1}和{2}为短信模板参数。
            String[] datas = {String.valueOf(code), "1"};
            String subAppend = "1234";  // 可选 扩展码，四位数字 0~9999
            String reqId = UUID.randomUUID().toString();  // 可选 第三方自定义消息id，最大支持32位英文数字，同账号下同一自然天内不允许重复
            // HashMap<String, Object> result = sdk.sendTemplateSMS(to,templateId,datas);
            HashMap<String, Object> result = sdk.sendTemplateSMS(to, templateId, datas, subAppend, reqId);
            if ("000000".equals(result.get("statusCode"))) {
                // 正常返回输出data包体信息（map）
                HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
                Set<String> keySet = data.keySet();
                for (String key : keySet) {
                    Object object = data.get(key);
                    log.info(key + " = " + object);
                }
            } else {
                // 异常返回输出错误码和错误信息
                log.error("错误码=" + result.get("statusCode") + " 错误信息= " + result.get("statusMsg"));
            }
            return true;
        }catch (Exception e) {
            log.error("[sendSmsToCCP] error is ", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 模拟发送短信过程，感兴趣的朋友可以尝试对接一些第三方的短信平台
     *
     * @param phone
     * @param code
     */
    private boolean mockSendSms(String phone, Integer code) {
        try {
            log.info(" ============= 创建短信发送通道中 ============= ,phone is {},code is {}", phone, code);
            Thread.sleep(1000);
            log.info(" ============= 短信已经发送成功 ============= ");
            return true;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
