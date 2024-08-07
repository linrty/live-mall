package top.linrty.live.living.utils;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import top.linrty.live.common.config.redis.RedisKeyBuilder;
import top.linrty.live.common.config.redis.RedisKeyLoadMatch;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/6 21:54
 * @Version: 1.0
 **/
@Configuration
@Conditional(RedisKeyLoadMatch.class)
public class GiftProviderCacheKeyBuilder extends RedisKeyBuilder {
    private static final String GIFT_CONFIG_CACHE = "gift_config_cache";
    private static final String GIFT_LIST_CACHE = "gift_list_cache";
    private static final String GIFT_CONSUME_KEY = "gift_consume_key";
    private static final String GIFT_LIST_LOCK = "gift_list_lock";
    private static final String LIVING_PK_KEY = "living_pk_key";
    private static final String LIVING_PK_SEND_SEQ = "living_pk_send_seq";
    private static final String LIVING_PK_IS_OVER = "living_pk_is_over";

    private static final String RED_PACKET_LIST = "red_packet_list";
    private static final String RED_PACKET_INIT_LOCK = "red_packet_init_lock";
    private static final String RED_PACKET_TOTAL_GET_COUNT = "red_packet_total_get_count";
    private static final String RED_PACKET_TOTAL_GET_PRICE = "red_packet_total_get_price";
    private static final String RED_PACKET_MAX_GET_PRICE = "red_packet_max_get_price";
    private static final String USER_TOTAL_GET_PRICE_CACHE = "red_packet_user_total_get_price";
    private static final String RED_PACKET_PREPARE_SUCCESS = "red_packet_prepare_success";
    private static final String RED_PACKET_NOTIFY = "red_packet_notify";

    public String buildLivingPkIsOver(Integer roomId) {
        return getPrefix() + LIVING_PK_IS_OVER + getSplitItem() + roomId;
    }

    public String buildLivingPkSendSeq(Integer roomId) {
        return getPrefix() + LIVING_PK_SEND_SEQ + getSplitItem() + roomId;
    }

    public String buildLivingPkKey(Integer roomId) {
        return getPrefix() + LIVING_PK_KEY + getSplitItem() + roomId;
    }

    public String buildGiftConsumeKey(String uuid) {
        return getPrefix() + GIFT_CONSUME_KEY + getSplitItem() + uuid;
    }

    public String buildGiftConfigCacheKey(int giftId) {
        return getPrefix() + GIFT_CONFIG_CACHE + getSplitItem() + giftId;
    }

    public String buildGiftListCacheKey() {
        return getPrefix() + GIFT_LIST_CACHE;
    }

    public String buildGiftListLockCacheKey() {
        return getPrefix() + GIFT_LIST_LOCK;
    }

    public String buildRedPacketList(String code) {
        return getPrefix() + RED_PACKET_LIST + getSplitItem() + code;
    }

    public String buildRedPacketInitLock(String code) {
        return getPrefix() + RED_PACKET_INIT_LOCK + getSplitItem() + code;
    }

    public String buildRedPacketTotalGetCount(String code) {
        return getPrefix() + RED_PACKET_TOTAL_GET_COUNT + getSplitItem() + (Math.abs(code.hashCode()) % 100);
    }

    public String buildRedPacketTotalGetPrice(String code) {
        return getPrefix() + RED_PACKET_TOTAL_GET_PRICE + getSplitItem() + (Math.abs(code.hashCode()) % 100);
    }

    public String buildRedPacketMaxGetPrice(String code) {
        return getPrefix() + RED_PACKET_MAX_GET_PRICE + getSplitItem() + (Math.abs(code.hashCode()) % 100);
    }

    public String buildUserTotalGetPrice(Long userId) {
        return getPrefix() + USER_TOTAL_GET_PRICE_CACHE + getSplitItem() + userId;
    }

    public String buildRedPacketPrepareSuccess(String code) {
        return getPrefix() + RED_PACKET_PREPARE_SUCCESS + getSplitItem() + code;
    }

    public String buildRedPacketNotify(String code) {
        return getPrefix() + RED_PACKET_NOTIFY + getSplitItem() + code;
    }
}
