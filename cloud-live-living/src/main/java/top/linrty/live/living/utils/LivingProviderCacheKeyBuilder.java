package top.linrty.live.living.utils;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import top.linrty.live.common.config.redis.RedisKeyBuilder;
import top.linrty.live.common.config.redis.RedisKeyLoadMatch;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/5 22:50
 * @Version: 1.0
 **/
@Component
@Conditional(RedisKeyLoadMatch.class)
public class LivingProviderCacheKeyBuilder extends RedisKeyBuilder {

    private static final String LIVING_ROOM_LIST = "living_room_list";

    private static final String LIVING_ROOM_LIST_REFRESH_FLAG = "living_room_list_refresh_flag";
    private static final String LIVING_ROOM_OBJ = "living_room_obj";

    private static final String REFRESH_LIVING_ROOM_LIST_LOCK = "refresh_living_room_list_lock";
    private static final String LIVING_ROOM_USER_SET = "living_room_user_set";
    private static final String LIVING_ONLINE_PK = "living_online_pk";

    public String buildLivingOnlinePk(Integer roomId) {
        return super.getPrefix() + LIVING_ONLINE_PK + super.getSplitItem() + roomId;
    }

    public String buildLivingRoomUserSet(Integer roomId, Integer appId) {
        return super.getPrefix() + LIVING_ROOM_USER_SET + super.getSplitItem() + appId + super.getSplitItem() + roomId;
    }

    public String buildRefreshLivingRoomListLock() {
        return super.getPrefix() + REFRESH_LIVING_ROOM_LIST_LOCK;
    }

    public String buildLivingRoomObj(Integer roomId) {
        return super.getPrefix() + LIVING_ROOM_OBJ + super.getSplitItem() + roomId;
    }

    public String buildLivingRoomList(Integer type) {
        return super.getPrefix() + LIVING_ROOM_LIST + super.getSplitItem() + type;
    }

    public String buildLivingRoomListRefreshFlag(Integer type) {
        return super.getPrefix() + LIVING_ROOM_LIST_REFRESH_FLAG + super.getSplitItem() + type;
    }
}
