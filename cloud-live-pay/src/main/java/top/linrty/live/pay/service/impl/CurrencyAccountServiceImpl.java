package top.linrty.live.pay.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.linrty.live.common.domain.dto.pay.AccountTradeReqDTO;
import top.linrty.live.common.domain.dto.pay.AccountTradeRespDTO;
import top.linrty.live.common.domain.vo.pay.CurrencyAccountVO;
import top.linrty.live.common.enums.pay.TradeTypeEnum;
import top.linrty.live.common.exception.UnknownException;
import top.linrty.live.common.utils.ConvertBeanUtils;
import top.linrty.live.pay.domain.po.CurrencyAccount;
import top.linrty.live.pay.mapper.CurrencyAccountMapper;
import top.linrty.live.pay.service.ICurrencyAccountService;
import top.linrty.live.pay.service.ICurrencyTradeService;
import top.linrty.live.pay.utils.PayProviderCacheKeyBuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/6 23:10
 * @Version: 1.0
 **/
@Service
@Slf4j
public class CurrencyAccountServiceImpl implements ICurrencyAccountService {

    @Resource
    private CurrencyAccountMapper currencyAccountMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private PayProviderCacheKeyBuilder payProviderCacheKeyBuilder;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private ICurrencyTradeService currencyTradeService;


    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 4, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000));

    @Override
    public boolean insertOne(Long userId) {
        try {
            CurrencyAccount accountPO = new CurrencyAccount();
            accountPO.setUserId(userId);
            currencyAccountMapper.insert(accountPO);
            return true;
        } catch (Exception e) {
            //有异常但是不抛出，只为了避免重复创建相同userId的账户
        }
        return false;
    }

    @Override
    public void incr(Long userId, int num) {
        String key = payProviderCacheKeyBuilder.buildUserBalance(userId);
        // 如果redis中存在缓存，基于redis的余额扣减
        if(Boolean.TRUE.equals(redisTemplate.hasKey(key))){
            redisTemplate.opsForValue().increment(key, num);
        }
        // DB层操作（包括余额增加和流水记录）
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // 在异步线程池中完成数据库层的增加和流水记录，带有事务
                // 异步操作：CAP中的AP，没有追求强一致性，保证最终一致性即可（BASE理论）
                incrDBHandler(userId, num);
            }
        });
        currencyAccountMapper.incr(userId, num);
    }

    @Override
    public void decr(Long userId, int num) {
        String key = payProviderCacheKeyBuilder.buildUserBalance(userId);
        // 1、基于redis的余额修改
        redisTemplate.opsForValue().decrement(key, num);
        // 2、 基于数据库的余额修改
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // 在异步线程池中完成数据库层的修改和流水记录
                // 没有追求强一致性，保证最终一致性即可
                consumeDBHandler(userId, num);
            }
        });
    }

    @Override
    @DS("read_db")
    public CurrencyAccountVO getByUserId(Long userId) {
        return ConvertBeanUtils.convert(currencyAccountMapper.selectById(userId), CurrencyAccountVO.class);
    }

    @Override
    @DS("read_db")
    public Integer getBalance(Long userId) {
        String cacheKey = payProviderCacheKeyBuilder.buildUserBalance(userId);
        Integer balance = (Integer) redisTemplate.opsForValue().get(cacheKey);
        if (balance != null) {
            if (balance == -1) {
                return null;
            }
            return balance;
        }
        balance = currencyAccountMapper.queryBalance(userId);
        if (balance == null) {
            redisTemplate.opsForValue().set(cacheKey, -1, 1L, TimeUnit.MINUTES);
            return null;
        }
        redisTemplate.opsForValue().set(cacheKey, balance, 30L, TimeUnit.MINUTES);
        return balance;
    }

    // 大并发请求场景，1000个直播间，500人，50W人在线，20%的人送礼，10W人在线触发送礼行为
    // DB扛不住
    // 1.MySQL换成写入性能相对较高的数据库
    // 2.我们能不能从业务上去进行优化，用户送礼都在直播间，大家都连接上了im服务器，router层扩容(50台)，im-core-server层(100台)，MQ削峰
    // 消费端也可以水平扩容
    // 3.我们客户端发起送礼行为的时候，同步校验（校验账户余额是否足够，余额放入到Redis中）
    // 4.拦下大部分的求，如果余额不足，（接口还得做防止重复点击，客户端也要放重复）
    // 5.同步送礼接口，只完成简单的余额校验，发送mq，在mq的异步操作里面，完成二次余额校验，余额扣减，礼物发送
    // 6.如果余额不足，是不是可以利用im，反向通知发送方，余额充足，利用im实现礼物特效推送
    @Override
    public AccountTradeRespDTO consumeForSendGift(AccountTradeReqDTO accountTradeReqDTO) {
        // 1 余额判断并在Redis中扣减余额
        Long userId = accountTradeReqDTO.getUserId();
        int num = accountTradeReqDTO.getNum();
        String lockKey = payProviderCacheKeyBuilder.buildUserBalanceLock(userId);
        RLock lock = redissonClient.getLock(lockKey);
        try{
            Boolean isLock = redisTemplate.opsForValue().setIfAbsent(lockKey, 1, 1L, TimeUnit.SECONDS);
            if (Boolean.FALSE.equals(isLock)){
                throw new UnknownException("获取锁失败");
            }
            Integer balance = this.getBalance(userId);
            if (balance == null || balance < num) {
                AccountTradeRespDTO res = new AccountTradeRespDTO()
                        .setCode(1)
                        .setMsg("账户余额不足")
                        .setUserId(userId)
                        .setSuccess(false);
                return res;
            }
            // 封装的方法：包括redis余额扣减和 异步DB层处理
            this.decr(userId, num);
        }catch (Exception e){
            try {
                Thread.sleep(ThreadLocalRandom.current().nextLong(500, 1000));
            } catch (InterruptedException interruptedException) {
                log.error(interruptedException.getMessage());
            }
            // 等待0.5~1秒后重试
            consumeForSendGift(accountTradeReqDTO);
        } finally {
            lock.unlock();
        }
        AccountTradeRespDTO res = new AccountTradeRespDTO();
        res.setUserId(userId)
                .setMsg("扣费成功")
                .setSuccess(true);
        return res;
    }

    // 发送礼物数据层的处理
    @Transactional(rollbackFor = Exception.class)
    public void consumeDBHandler(Long userId, int num) {
        // 扣减余额(DB层)
        currencyAccountMapper.decr(userId, num);
        // 流水记录
        currencyTradeService.insertOne(userId, num * -1, TradeTypeEnum.SEND_GIFT_TRADE.getCode());
    }

    @Transactional(rollbackFor = Exception.class)
    public void incrDBHandler(Long userId, int num) {
        // 扣减余额(DB层)
        currencyAccountMapper.incr(userId, num);
        // 流水记录
        currencyTradeService.insertOne(userId, num, TradeTypeEnum.SEND_GIFT_TRADE.getCode());
    }
}

