package top.linrty.live.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import top.linrty.live.common.domain.dto.user.MsgCheckDTO;
import top.linrty.live.common.domain.dto.user.UserDTO;
import top.linrty.live.common.domain.dto.user.UserLoginVO;
import top.linrty.live.common.domain.po.KafkaObject;
import top.linrty.live.common.domain.vo.CommonRespVO;
import top.linrty.live.common.enums.KafkaCodeEnum;
import top.linrty.live.common.enums.MsgSendResultEnum;
import top.linrty.live.common.exception.ParamException;
import top.linrty.live.common.exception.SystemException;
import top.linrty.live.common.exception.UnknownException;
import top.linrty.live.common.utils.JwtUtils;
import top.linrty.live.common.utils.RedisSeqIdHelper;
import top.linrty.live.user.domain.po.User;
import top.linrty.live.user.mapper.IUserMapper;
import top.linrty.live.user.service.ISmsService;
import top.linrty.live.user.service.IUserService;
import top.linrty.live.user.utils.UserProviderCacheKeyBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/7/20 17:40
 * @Version: 1.0
 **/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<IUserMapper, User> implements IUserService {

    @Resource
    private RedisTemplate<String, UserDTO> redisTemplate;

    @Resource
    private UserProviderCacheKeyBuilder userProviderCacheKeyBuilder;

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Resource
    private ISmsService smsService;

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private RedisSeqIdHelper redisSeqIdHelper;

    private static final String PHONE_REG = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";


    @Override
    public void sendLoginCode(String phone) {
        // 参数校验
        if (StrUtil.isEmpty(phone)) {
            throw new ParamException("手机号不能为空");
        }
        if (!Pattern.matches(PHONE_REG, phone)) {
            throw new ParamException("手机号格式错误");
        }
        MsgSendResultEnum msgSendResultEnum = smsService.sendLoginCode(phone);
        if (msgSendResultEnum != MsgSendResultEnum.SEND_SUCCESS) {
            throw new SystemException("短信发送太频繁，请稍后再试");
        }
    }

    @Override
    public UserLoginVO login(String phone, Integer code){
        // 参数校验
        if (StrUtil.isEmpty(phone)) {
            throw new ParamException("手机号不能为空");
        }
        if (!Pattern.matches(PHONE_REG, phone)) {
            throw new ParamException("手机号格式错误");
        }
        if (code == null || code < 1000) {
            throw new ParamException("验证码格式异常");
        }
        // 检查验证码是否匹配
        MsgCheckDTO msgCheckDTO = smsService.checkLoginCode(phone, code);
        if (!msgCheckDTO.isCheckStatus()) {// 校验没通过
            throw new UnknownException(msgCheckDTO.getDesc());
        }
        // 检查用户是否注册过
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        User user = this.getOne(queryWrapper);
        if(user == null) {
            // 没有注册的新用户
            user = new User();
            user.setPhone(phone)
                    .setUserId(redisSeqIdHelper.nextId("user_id"))
                    .setNickName("live-" + ThreadLocalRandom.current().nextInt(1000, 9999));
            this.save(user);
        }
        Map<String, Object> param =  new HashMap<>();
        param.put("userId", user.getUserId());
        String token = jwtUtils.createJWT(param);
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setToken(token)
                .setUserId(user.getUserId());
        return userLoginVO;
    }

    @Override
    @DS("read_db")
    public UserDTO getUserById(Long userId) {
        if(userId == null) {
            return null;
        }
        String key = userProviderCacheKeyBuilder.buildUserInfoKey(userId);
        UserDTO userDTO = redisTemplate.opsForValue().get(key);
        if(userDTO != null) {
            return userDTO;
        }
        userDTO = BeanUtil.copyProperties(baseMapper.selectById(userId), UserDTO.class);
        if(userDTO != null) {
            redisTemplate.opsForValue().set(key, userDTO, 30L, TimeUnit.MINUTES);
        }
        return userDTO;
    }

    @Override
    @DS("write_db")
    public boolean updateUserInfo(UserDTO userDTO) {
        if(userDTO == null || userDTO.getUserId() == null) {
            return false;
        }
        baseMapper.updateById(BeanUtil.copyProperties(userDTO, User.class));
        //更改操作，删除缓存
        redisTemplate.delete(userProviderCacheKeyBuilder.buildUserInfoKey(userDTO.getUserId()));
        //TODO 计划更改为canal实现延迟双删或双写
        KafkaObject kafkaObject = new KafkaObject(KafkaCodeEnum.USER_INFO.getCode(), userDTO.getUserId().toString());
        kafkaTemplate.send("user-delete-cache", JSONUtil.toJsonStr(kafkaObject));
        log.info("Kafka发送延迟双删消息成功，用户ID：{}", userDTO.getUserId());
        return true;
    }

    @Override
    @DS("write_db")
    public boolean insertOne(UserDTO userDTO) {
        if(userDTO == null || userDTO.getUserId() == null) {
            return false;
        }
        baseMapper.insert(BeanUtil.copyProperties(userDTO, User.class));
        return true;
    }

    @Override
    @DS("read_db")
    public Map<Long, UserDTO> batchQueryUserInfo(List<Long> userIdList) {
        if(CollectionUtil.isEmpty(userIdList)) {
            return Collections.emptyMap();
        }
        //user的id都大于10000
        userIdList = userIdList.stream().filter(id -> id > 10000).collect(Collectors.toList());
        if(CollectionUtil.isEmpty(userIdList)) {
            return Collections.emptyMap();
        }

        //先查询Redis缓存
        List<String> multiKeyList = userIdList.stream()
                .map(userId -> userProviderCacheKeyBuilder.buildUserInfoKey(userId)).collect(Collectors.toList());
        List<UserDTO> userDTOList = redisTemplate.opsForValue().multiGet(multiKeyList).stream().filter(x -> x != null).collect(Collectors.toList());
        //若Redis查询出来的数据数量和要查询的数量相等，直接返回
        if(!CollectionUtil.isEmpty(userDTOList) && userDTOList.size() == userIdList.size()) {
            return userDTOList.stream().collect(Collectors.toMap(UserDTO::getUserId, userDTO -> userDTO));
        }
        //不相等，去MySQL查询无缓存的数据
        List<Long> userIdInCacheList = userDTOList.stream().map(UserDTO::getUserId).collect(Collectors.toList());
        List<Long> userIdNotInCacheList = userIdList.stream().filter(userId -> !userIdInCacheList.contains(userId)).collect(Collectors.toList());
        //为了防止sharding-jdbc笛卡尔积路由，对id进行分组
        Map<Long, List<Long>> userIdMap = userIdNotInCacheList.stream().collect(Collectors.groupingBy(userId -> userId % 100));
        List<UserDTO> dbQueryList = new CopyOnWriteArrayList<>();
        userIdMap.values().parallelStream().forEach(queryUserIdList -> {
            dbQueryList.addAll(BeanUtil.copyToList(baseMapper.selectBatchIds(queryUserIdList), UserDTO.class));
        });
        //查询MySQL不为空，缓存进Redis
        if(!CollectionUtil.isEmpty(dbQueryList)) {
            Map<String, UserDTO> multiSaveMap = dbQueryList.stream().collect(Collectors.toMap(userDTO -> userProviderCacheKeyBuilder.buildUserInfoKey(userDTO.getUserId()), x -> x));
            redisTemplate.opsForValue().multiSet(multiSaveMap);
            //mset不能设置过期时间，使用管道设置，减少网路IO
            redisTemplate.executePipelined(new SessionCallback<Object>() {
                @Override
                public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                    for (String key : multiSaveMap.keySet()) {
                        operations.expire((K) key, createRandomExpireTime(), TimeUnit.SECONDS);
                    }
                    return null;
                }
            });
            userDTOList.addAll(dbQueryList);
        }
        return userDTOList.stream().collect(Collectors.toMap(UserDTO::getUserId, userDTO -> userDTO));
    }

    //生成随机过期时间，单位：秒
    private long createRandomExpireTime() {
        return ThreadLocalRandom.current().nextLong(1000) + 60 * 30;//30min + 1000s
    }
}
