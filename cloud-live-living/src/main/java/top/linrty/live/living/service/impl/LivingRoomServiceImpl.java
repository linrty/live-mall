package top.linrty.live.living.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.linrty.live.api.clients.RouterClient;
import top.linrty.live.api.clients.UserClient;
import top.linrty.live.common.config.redis.RedisKeyTime;
import top.linrty.live.common.constants.HTTPKeyConstants;
import top.linrty.live.common.constants.living.GiftTopicNames;
import top.linrty.live.common.domain.dto.PageReqDTO;
import top.linrty.live.common.domain.dto.im.IMOfflineDTO;
import top.linrty.live.common.domain.dto.im.IMOnlineDTO;
import top.linrty.live.common.domain.dto.living.LivingRoomReqDTO;
import top.linrty.live.common.domain.dto.living.LivingRoomRespDTO;
import top.linrty.live.common.domain.dto.living.OnlinePKReqDTO;
import top.linrty.live.common.domain.dto.user.UserDTO;
import top.linrty.live.common.domain.po.im.IMMsgBody;
import top.linrty.live.common.domain.vo.PageRespVO;
import top.linrty.live.common.domain.vo.living.LivingPkRespVO;
import top.linrty.live.common.enums.BizEnum;
import top.linrty.live.common.enums.StatusEnum;
import top.linrty.live.common.enums.im.IMMsgBizCodeEnum;
import top.linrty.live.common.exception.UnknownException;
import top.linrty.live.common.utils.ConvertBeanUtils;
import top.linrty.live.common.utils.RequestContext;
import top.linrty.live.living.domain.po.LivingRoom;
import top.linrty.live.living.domain.po.LivingRoomRecord;
import top.linrty.live.common.domain.vo.living.LivingRoomInitVO;
import top.linrty.live.common.domain.vo.living.LivingRoomRespVO;
import top.linrty.live.living.mapper.LivingRoomMapper;
import top.linrty.live.living.mapper.LivingRoomRecordMapper;
import top.linrty.live.living.service.ILivingRoomService;
import top.linrty.live.living.utils.GiftProviderCacheKeyBuilder;
import top.linrty.live.living.utils.LivingProviderCacheKeyBuilder;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static top.linrty.live.common.constants.HTTPKeyConstants.HTTP_HEADER_USER_ID;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/4 23:40
 * @Version: 1.0
 **/
@Service
@Slf4j
public class LivingRoomServiceImpl implements ILivingRoomService {

    @Resource
    private LivingRoomMapper livingRoomMapper;
    @Resource
    private LivingRoomRecordMapper livingRoomRecordMapper;

    @DubboReference
    private UserClient userClient;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private LivingProviderCacheKeyBuilder livingProviderCacheKeyBuilder;

    @Resource
    private RedissonClient redissonClient;


    @Resource
    private RouterClient routerClient;

    @Resource
    private GiftProviderCacheKeyBuilder giftProviderCacheKeyBuilder;

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public Integer startingLiving(Integer type) {
        String userIdStr = RequestContext.get(HTTP_HEADER_USER_ID).toString();
        Long userId = Long.parseLong(userIdStr);
        UserDTO userDTO = userClient.getUserById(userId);
        LivingRoom livingRoom = new LivingRoom();
        livingRoom.setAnchorId(userId)
                .setRoomName("主播-" + userId + "的直播间")
                .setCovertImg(userDTO.getAvatar())
                .setType(type)
                .setStartTime(new Date())
                .setStatus(StatusEnum.VALID_STATUS.getCode());
        livingRoomMapper.insert(livingRoom);
        String key = livingProviderCacheKeyBuilder.buildLivingRoomObj(livingRoom.getId());
        // 防止之前有空值缓存，这里做移除操作
        redisTemplate.delete(key);
        addRoomToRedis(livingRoom);
        // 发送mq进行异步商品库存加载
        kafkaTemplate.send(GiftTopicNames.START_LIVING_ROOM, String.valueOf(livingRoom.getAnchorId()));
        return livingRoom.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean closeLiving(Integer roomId) {
        LivingRoomReqDTO livingRoomReqDTO = new LivingRoomReqDTO();
        livingRoomReqDTO.setRoomId(roomId);
        livingRoomReqDTO.setAnchorId(Long.parseLong(RequestContext.get(HTTP_HEADER_USER_ID).toString()));
        LivingRoom livingRoom = livingRoomMapper.selectById(livingRoomReqDTO.getId());
        if (livingRoom == null) return false;
        if (!livingRoomReqDTO.getAnchorId().equals(livingRoom.getAnchorId())) return false;
        LivingRoomRecord livingRoomRecord = BeanUtil.copyProperties(livingRoom, LivingRoomRecord.class);
        livingRoomRecord.setEndTime(new Date()).setStatus(StatusEnum.INVALID_STATUS.getCode());
        livingRoomRecordMapper.insert(livingRoomRecord);
        livingRoomRecordMapper.deleteById(livingRoom.getId());
        // 移除掉直播间cache
        String key = livingProviderCacheKeyBuilder.buildLivingRoomObj(livingRoom.getId());
        redisTemplate.delete(key);
        // 设置刷新标记
        String refreshKey = livingProviderCacheKeyBuilder.buildLivingRoomListRefreshFlag(livingRoom.getType());
        redisTemplate.opsForValue().set(refreshKey, true);
        return true;
    }

    @Override
    public LivingRoomRespDTO queryByRoomId(Integer roomId) {
        String key = livingProviderCacheKeyBuilder.buildLivingRoomObj(roomId);
        LivingRoomRespDTO respDTO = (LivingRoomRespDTO) redisTemplate.opsForValue().get(key);
        if (respDTO != null){
            if (respDTO.getId() == null){
                // 空值缓存
                return null;
            }
            return respDTO;
        }
        LambdaQueryWrapper<LivingRoom> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LivingRoom::getId, roomId);
        queryWrapper.eq(LivingRoom::getStatus, StatusEnum.VALID_STATUS.getCode());
        queryWrapper.last("limit 1");
        LivingRoom livingRoom = livingRoomMapper.selectOne(queryWrapper);
        respDTO = BeanUtil.copyProperties(livingRoom, LivingRoomRespDTO.class);
        if (respDTO == null){
            // 防止缓存穿透
            redisTemplate.opsForValue().set(key, new LivingRoomRespDTO(), RedisKeyTime.EXPIRE_TIME_ONE_MINUTE, TimeUnit.SECONDS);
            return null;
        }
        redisTemplate.opsForValue().set(key, respDTO, RedisKeyTime.EXPIRE_TIME_HALF_HOUR, TimeUnit.SECONDS);
        return respDTO;
    }

    @Override
    public LivingRoomInitVO anchorConfig(Long userId, Integer roomId) {
        LivingRoomRespDTO respDTO = queryByRoomId(roomId);
        Map<Long, UserDTO> userDTOMap = userClient.batchQueryUserInfo(
                Arrays.asList(respDTO.getAnchorId(), userId)
                        .stream()
                        .distinct()
                        .collect(Collectors.toList())
        );
        UserDTO anchor = userDTOMap.get(respDTO.getAnchorId());
        UserDTO watcher = userDTOMap.get(userId);
        LivingRoomInitVO respVO = new LivingRoomInitVO();
        respVO.setAnchorNickName(anchor.getNickName())
                .setWatcherNickName(watcher.getNickName())
                .setUserId(userId)
                .setAvatar(anchor.getAvatar())
                .setWatcherAvatar(watcher.getAvatar());
        if (respDTO == null || respDTO.getAnchorId() == null || userId == null) {
            respVO.setRoomId(-1);
        }else {
            respVO.setRoomId(respDTO.getId())
                    .setRoomName(respDTO.getRoomName())
                    .setAnchorId(respDTO.getAnchorId())
                    .setAnchor(respDTO.getAnchorId().equals(userId));
        }
        return respVO;
    }

    @Override
    public PageRespVO<LivingRoomRespVO> list(PageReqDTO<LivingRoomReqDTO> pageReqDTO) {
        // 因为直播平台同时在线人数不算太多，属于读多写少的场景。缓存进redis
        String key = livingProviderCacheKeyBuilder.buildLivingRoomList(pageReqDTO.getData().getType());
        long page = pageReqDTO.getPage();
        long pageSize = pageReqDTO.getPageSize();
        long total = redisTemplate.opsForList().size(key);
        List<Object> resultList = redisTemplate.opsForList().range(key, (page - 1) * pageSize, page * pageSize);
        PageRespVO<LivingRoomRespVO> pageRespVO = new PageRespVO<>();
        if (CollectionUtils.isEmpty(resultList)){
            pageRespVO.setHasNext(false)
                    .setList(Collections.emptyList())
                    .setPage(page)
                    .setPageSize(pageSize)
                    .setTotal(total)
                    .setTotalPage(total / pageSize);
        }else{
            pageRespVO.setHasNext(total > page * pageSize)
                    .setList(ConvertBeanUtils.convertList(resultList, LivingRoomRespVO.class))
                    .setPage(page)
                    .setPageSize(pageSize)
                    .setTotal(total)
                    .setTotalPage(total / pageSize);
        }
        return pageRespVO;

    }

    @Override
    public List<LivingRoomRespVO> listAllLivingRoomFromDB(Integer type) {
        LambdaQueryWrapper<LivingRoom> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LivingRoom::getType, type);
        queryWrapper.eq(LivingRoom::getStatus, StatusEnum.VALID_STATUS.getCode());
        queryWrapper.orderByDesc(LivingRoom::getStartTime);
        return ConvertBeanUtils.convertList(livingRoomMapper.selectList(queryWrapper), LivingRoomRespVO.class);
    }

    /**
     * 添加新开播的房间到缓存中,因为开播的实时性比较重要，所以在第一时间需要同步到redis中
     *
     * @param livingRoom
     */
    private void addRoomToRedis(LivingRoom livingRoom) {
        // 先获取缓存中所有的开播房间
        // 找到对应的列表
        // 先获取锁
        String lockKey = livingProviderCacheKeyBuilder.buildRefreshLivingRoomListLock();
        RLock lock =redissonClient.getLock(lockKey);
        try{
            boolean isLock = lock.tryLock(3L, RedisKeyTime.EXPIRE_TIME_ONE_MINUTE, TimeUnit.SECONDS);
            if (BooleanUtil.isFalse(isLock)){
                return;
            }
            String key = livingProviderCacheKeyBuilder.buildLivingRoomList(livingRoom.getType());
            // 添加到缓存中，因为是最新开播的所以放在最前面
            redisTemplate.opsForList().leftPush(key, livingRoom);
        }catch (Exception e){
            log.error("获取锁失败", e);
        } finally {
            lock.unlock();
        }
    }


    @Override
    public void userOnlineHandler(IMOnlineDTO imOnlineDTO) {
        Long userId = imOnlineDTO.getUserId();
        Integer appId = imOnlineDTO.getAppId();
        Integer roomId = imOnlineDTO.getRoomId();
        String cacheKey = livingProviderCacheKeyBuilder.buildLivingRoomUserSet(roomId, appId);
        redisTemplate.opsForSet().add(cacheKey, userId);
        redisTemplate.expire(cacheKey, 12L, TimeUnit.HOURS);
    }

    @Override
    public void userOfflineHandler(IMOfflineDTO imOfflineDTO) {
        Long userId = imOfflineDTO.getUserId();
        Integer appId = imOfflineDTO.getAppId();
        Integer roomId = imOfflineDTO.getRoomId();
        String cacheKey = livingProviderCacheKeyBuilder.buildLivingRoomUserSet(roomId, appId);
        redisTemplate.opsForSet().remove(cacheKey, userId);

        // 新增PK直播间 下线调用
        LivingRoomReqDTO reqDTO = new LivingRoomReqDTO();
        reqDTO.setRoomId(roomId);
        reqDTO.setPkObjId(userId);
        this.offlinePk(reqDTO);
    }

    @Override
    public List<Long> queryUserIdsByRoomId(LivingRoomReqDTO livingRoomReqDTO) {
        Integer roomId = livingRoomReqDTO.getRoomId();
        Integer appId = livingRoomReqDTO.getAppId();
        String cacheKey = livingProviderCacheKeyBuilder.buildLivingRoomUserSet(roomId, appId);
        //使用scan分批查询数据，否则set元素太多容易造成redis和网络阻塞（scan会自动分成多次请求去执行）
        Cursor<Object> cursor = redisTemplate.opsForSet().scan(cacheKey, ScanOptions.scanOptions().match("*").count(100).build());
        List<Long> userIdList = new ArrayList<>();
        while (cursor.hasNext()) {
            userIdList.add((Long) cursor.next());
        }
        return userIdList;
    }

    @Override
    public boolean onlinePK(OnlinePKReqDTO onlinePKReqDTO) {
        LivingRoomReqDTO reqDTO = new LivingRoomReqDTO();
        reqDTO.setRoomId(onlinePKReqDTO.getRoomId());
        reqDTO.setAppId(BizEnum.LIVE_BIZ.getCode());
        reqDTO.setPkObjId(Long.parseLong(RequestContext.get(HTTP_HEADER_USER_ID).toString()));
        LivingRoomRespDTO livingRoomRespDTO = queryByRoomId(reqDTO.getRoomId());
        LivingPkRespVO respVO = new LivingPkRespVO();
        respVO.setOnlineStatus(false);
        if (livingRoomRespDTO.getAnchorId().equals(reqDTO.getPkObjId())) {
            respVO.setMsg("主播不可以连线参与PK");
        }else{
            String cacheKey = livingProviderCacheKeyBuilder.buildLivingOnlinePk(onlinePKReqDTO.getRoomId());
            // 使用setIfAbsent防止被后来者覆盖
            Boolean tryOnline = redisTemplate.opsForValue().setIfAbsent(cacheKey, reqDTO.getPkObjId(), 12L, TimeUnit.HOURS);
            if (Boolean.TRUE.equals(tryOnline)){
                // 通知直播间所有人，有人上线PK了
                List<Long> userIdList = queryUserIdsByRoomId(reqDTO);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("pkObjId", reqDTO.getPkObjId());
                jsonObject.put("pkObjAvatar", "../svga/img/爱心.png");
                batchSendImMsg(userIdList, IMMsgBizCodeEnum.LIVING_ROOM_PK_ONLINE.getCode(), jsonObject);
                respVO.setMsg("连线成功");
                respVO.setOnlineStatus(true);
            }else{
                respVO.setMsg("主播正在PK中");
            }
        }
        if (Boolean.FALSE.equals(respVO.isOnlineStatus())){
            throw new UnknownException(respVO.getMsg());
        }
        return true;
    }

    @Override
    public boolean offlinePk(LivingRoomReqDTO livingRoomReqDTO) {
        Integer roomId = livingRoomReqDTO.getRoomId();
        Long pkObjId = this.queryOnlinePkUserId(roomId);
        // 如果他是pkObjId本人，才删除
        if (!livingRoomReqDTO.getPkObjId().equals(pkObjId)) {
            System.out.println("删除失败");
            return false;
        }
        System.out.println("删除成功");
        String cacheKey = livingProviderCacheKeyBuilder.buildLivingOnlinePk(roomId);
        String key = giftProviderCacheKeyBuilder.buildLivingPkKey(roomId);
        //删除PK进度条值缓存
        redisTemplate.delete(key);
        //删除PK直播间pkObjId缓存
        return Boolean.TRUE.equals(redisTemplate.delete(cacheKey));
    }

    @Override
    public Long queryOnlinePkUserId(Integer roomId) {
        String cacheKey = livingProviderCacheKeyBuilder.buildLivingOnlinePk(roomId);
        return (Long) redisTemplate.opsForValue().get(cacheKey);
    }

    @Override
    public LivingRoomRespDTO queryByAnchorId(Long anchorId) {
        LambdaQueryWrapper<LivingRoom> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LivingRoom::getAnchorId, anchorId);
        queryWrapper.eq(LivingRoom::getStatus, StatusEnum.VALID_STATUS.getCode());
        queryWrapper.last("limit 1");
        return ConvertBeanUtils.convert(livingRoomMapper.selectOne(queryWrapper), LivingRoomRespDTO.class);
    }

    /**
     * 批量发送im消息
     */
    private void batchSendImMsg(List<Long> userIdList, Integer bizCode, JSONObject jsonObject) {
        List<IMMsgBody> imMsgBodies = userIdList.stream().map(userId -> {
            IMMsgBody imMsgBody = new IMMsgBody();
            imMsgBody.setAppId(BizEnum.LIVE_BIZ.getCode());
            imMsgBody.setBizCode(bizCode);
            imMsgBody.setData(jsonObject.toJSONString());
            imMsgBody.setUserId(userId);
            return imMsgBody;
        }).collect(Collectors.toList());
        routerClient.batchSendMsg(imMsgBodies);
    }
}
