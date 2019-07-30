package com.newland.paas.sbcommon.utils.redis;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.newland.paas.common.exception.ErrorCode;
import com.newland.paas.common.exception.NLCheckedException;
import com.newland.paas.common.exception.NLUnCheckedException;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.sbcommon.utils.SpringContextUtil;

/**
 * redis操作封装
 * 
 * @author Administrator
 *
 */
public class RedisUtil {
    private static final Log log = LogFactory.getLogger(RedisUtil.class);
    private static final String tag = "RedisUtil===";

    private static final String errorCodeConn = ErrorCode.BUSERROR + "2100001"; // 连接获取异常
    private static final String errorCodeOp = ErrorCode.BUSERROR + "2100002"; // 操作异常
    private static final String errorCodeLockLoser = ErrorCode.BUSERROR + "2100003"; // 加锁失败

    private static RedisTemplate<String, Object> getRedisTemplate() throws NLUnCheckedException {
        try {
            return SpringContextUtil.getContext().getBean("redisTemplate", RedisTemplate.class);
        } catch (Exception e) {
            log.error(LogProperty.LOGTYPE_SYS, null, e, tag + "get RedisTemplate error");
            throw new NLUnCheckedException(errorCodeConn, "get RedisTemplate error", e);
        }
    }

    /**
     * 写入数据
     * 
     * @param key
     * @param value
     * @throws NLCheckedException
     */
    public static void set(String key, String value) throws NLUnCheckedException {
        try {
            getRedisTemplate().opsForValue().set(key, value);
        } catch (Exception e) {
            log.error(LogProperty.LOGTYPE_SYS, null, e, tag + "redis set value error");
            throw new NLUnCheckedException(errorCodeOp, "redis set value error", e);
        }
    }

    /**
     * 写入数据
     * 
     * @param key
     * @param value
     * @param expire 存活时间(秒)
     * @throws NLCheckedException
     */
    public static void set(String key, String value, long expire) throws NLUnCheckedException {
        try {
            RedisTemplate<String, Object> rdisTemplate = getRedisTemplate();
            rdisTemplate.opsForValue().set(key, value);
            rdisTemplate.expire(key, expire, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error(LogProperty.LOGTYPE_SYS, null, e, tag + "redis set value error");
            throw new NLUnCheckedException(errorCodeOp, "redis set value error", e);
        }
    }

    /**
     * 追加字符串
     * 
     * @param key
     * @param value
     * @throws NLUnCheckedException
     */
    public static void append(String key, String value) throws NLUnCheckedException {
        try {
            getRedisTemplate().opsForValue().append(key, value);
        } catch (Exception e) {
            log.error(LogProperty.LOGTYPE_SYS, null, e, tag + "redis set value error");
            throw new NLUnCheckedException(errorCodeOp, "redis set value error", e);
        }
    }

    /**
     * 获取数据
     * 
     * @param key
     * @return
     * @throws NLCheckedException
     */
    public static Object get(String key) throws NLUnCheckedException {
        try {
            return getRedisTemplate().opsForValue().get(key);
        } catch (Exception e) {
            log.error(LogProperty.LOGTYPE_SYS, null, e, tag + "redis get value error");
            throw new NLUnCheckedException(errorCodeOp, "redis set value error", e);
        }
    }

    /**
     * 获取数据长度
     * 
     * @param key
     * @return
     * @throws NLUnCheckedException
     */
    public static long size(String key) throws NLUnCheckedException {
        try {
            return getRedisTemplate().opsForValue().size(key);
        } catch (Exception e) {
            log.error(LogProperty.LOGTYPE_SYS, null, e, tag + "redis get value error");
            throw new NLUnCheckedException(errorCodeOp, "redis set value error", e);
        }
    }

    /**
     * 删除数据
     * 
     * @param key
     * @throws NLCheckedException
     */
    public static void del(String key) throws NLUnCheckedException {
        try {
            getRedisTemplate().delete(key);
        } catch (Exception e) {
            log.error(LogProperty.LOGTYPE_SYS, null, e, tag + "redis del value error");
            throw new NLUnCheckedException(errorCodeOp, "redis del value error", e);
        }
    }

    /**
     * 写入map数据
     * 
     * @param key
     * @param hashKey
     * @param value
     */
    public static void hset(String key, String hashKey, Object value) throws NLUnCheckedException {
        try {
            getRedisTemplate().opsForHash().put(key, hashKey, value);
        } catch (Exception e) {
            log.error(LogProperty.LOGTYPE_SYS, null, e, tag + "redis hset error");
            throw new NLUnCheckedException(errorCodeOp, "redis hset error", e);
        }
    }

    public static void hset(String key, Map<String, Object> m) throws NLUnCheckedException {
        try {
            getRedisTemplate().opsForHash().putAll(key, m);
        } catch (Exception e) {
            log.error(LogProperty.LOGTYPE_SYS, null, e, tag + "redis hset error");
            throw new NLUnCheckedException(errorCodeOp, "redis hset error", e);
        }
    }

    /**
     * 删除map中的KEY
     * 
     * @param key
     * @param hashKeys
     * @throws NLCheckedException
     */
    public static void hdel(String key, String... hashKeys) throws NLUnCheckedException {
        try {
            getRedisTemplate().opsForHash().delete(key, hashKeys);
        } catch (Exception e) {
            log.error(LogProperty.LOGTYPE_SYS, null, e, tag + "redis hdel error");
            throw new NLUnCheckedException(errorCodeOp, "redis hdel error", e);
        }
    }

    /**
     * 获取map中key对应的值
     * 
     * @param key
     * @param hashKey
     * @throws NLCheckedException
     */
    public static Object hget(String key, String hashKey) throws NLUnCheckedException {
        try {
            return getRedisTemplate().opsForHash().get(key, hashKey);
        } catch (Exception e) {
            log.error(LogProperty.LOGTYPE_SYS, null, e, tag + "redis hget error");
            throw new NLUnCheckedException(errorCodeOp, "redis hget error", e);
        }
    }

    /**
     * 获取缓存中的map
     * 
     * @param key
     * @return
     * @throws NLCheckedException
     */
    public static Map hgetAll(String key) throws NLUnCheckedException {
        try {
            return getRedisTemplate().opsForHash().entries(key);
        } catch (Exception e) {
            log.error(LogProperty.LOGTYPE_SYS, null, e, tag + "redis hget error");
            throw new NLUnCheckedException(errorCodeOp, "redis hgetAll error", e);
        }
    }

    /**
     * 获取map数据条数
     * 
     * @param key
     * @return
     * @throws NLCheckedException
     */
    public static long hsize(String key) throws NLUnCheckedException {
        try {
            return getRedisTemplate().opsForHash().size(key);
        } catch (Exception e) {
            log.error(LogProperty.LOGTYPE_SYS, null, e, tag + "redis hsize error");
            throw new NLUnCheckedException(errorCodeOp, "redis hsize error", e);
        }
    }

    /**
     * 写入list数据
     * 
     * @param key
     * @param value
     * @throws NLCheckedException
     */
    public static void ladd(String key, Object value) throws NLUnCheckedException {
        try {
            getRedisTemplate().opsForList().rightPush(key, value);
        } catch (Exception e) {
            log.error(LogProperty.LOGTYPE_SYS, null, e, tag + "redis ladd error");
            throw new NLUnCheckedException(errorCodeOp, "redis ladd error", e);
        }
    }

    public static void ladd(String key, List l) throws NLUnCheckedException {
        try {
            getRedisTemplate().opsForList().rightPushAll(key, l);
        } catch (Exception e) {
            log.error(LogProperty.LOGTYPE_SYS, null, e, tag + "redis ladd error");
            throw new NLUnCheckedException(errorCodeOp, "redis ladd error", e);
        }
    }

    /**
     * 获取list
     * 
     * @param key
     * @return
     * @throws NLCheckedException
     */
    public static List lgetAll(String key) throws NLUnCheckedException {
        try {
            return getRedisTemplate().opsForList().range(key, 0, -1);
        } catch (Exception e) {
            log.error(LogProperty.LOGTYPE_SYS, null, e, tag + "redis lgetAll error");
            throw new NLUnCheckedException(errorCodeOp, "redis lgetAll error", e);
        }
    }

    /**
     * 获取list条目数
     * 
     * @param key
     * @return
     * @throws NLCheckedException
     */
    public static long lsize(String key) throws NLUnCheckedException {
        try {
            return getRedisTemplate().opsForList().size(key);
        } catch (Exception e) {
            log.error(LogProperty.LOGTYPE_SYS, null, e, tag + "redis lgetAll error");
            throw new NLUnCheckedException(errorCodeOp, "redis lgetAll error", e);
        }
    }

    private static final long defaultTimeOut = 60 * 1; // 默认超时时间，1分钟，1分钟后自动解锁
    private static final long defaultWaitTime = 10000; // 默认等待解锁时间，10秒加锁失败则抛出异常

    /**
     * 加锁 使用动态生成UUID和默认加锁过期时间,加锁超时时间
     * 
     * @param key
     * @return
     */
    public static String lock(String key) {
        return lock(key, defaultWaitTime);
    }

    public static String lock(String key, long waitTime) {
        return lock(key, waitTime, defaultTimeOut);
    }

    public static String lock(String key, long waitTime, long timeOut) {
        return lock(key, null, waitTime, timeOut);
    }

    /**
     * 对已加锁KEY再次加锁必须用此方法
     * 
     * @param key
     * @param lockValue
     * @param timeOut
     * @return 加锁值，如果返回为null，则表示加锁失败
     */
    public static String lock(String key, String lockValue, long waitTime, long timeOut) {
        synchronized (key) {
            if (lockValue == null) {
                lockValue = UUID.randomUUID().toString();
            }
            long startTime = System.currentTimeMillis();
            while (true) {
                if (checkLock(key, lockValue)) {
                    // 未被加锁
                    break;
                } else {
                    // 已经被加锁，且不是该lockValue加锁
                    // 检查加锁等待时间是否已超时
                    if (System.currentTimeMillis() - startTime > waitTime) {
                        throw new NLUnCheckedException(errorCodeLockLoser, "redis lock key:" + key + " loser");
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
            RedisTemplate<String, Object> rdisTemplate = getRedisTemplate();
            ValueOperations<String, Object> ValueOperations = rdisTemplate.opsForValue();
            // 加锁
            boolean isLock = ValueOperations.setIfAbsent(key, lockValue);
            if (isLock) {
                // 加锁成功，设置超时时间
                rdisTemplate.expire(key, timeOut, TimeUnit.SECONDS);
                return lockValue;
            } else {
                return null;
            }
        }
    }

    /**
     * 解锁
     * 
     * @param key
     * @param lockValue
     * @return
     */
    public static boolean unLock(String key, String lockValue) {
        synchronized (key) {
            if (!checkLock(key, lockValue)) {
                // 已经被加锁，且不是该lockValue加锁
                return false;
            }
            del(key);
            return true;
        }
    }

    private static boolean checkLock(String key, String lockValue) {
        String lockValueR = (String)getRedisTemplate().opsForValue().get(key);
        if (lockValueR == null || lockValueR.equals(lockValue)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 将给定的消息发布到给定的通道
     * 
     * @param key
     * @param msg
     */
    public static void publish(String key, String msg) {
        try {
            getRedisTemplate().convertAndSend(key, msg);
        } catch (Exception e) {
            log.error(LogProperty.LOGTYPE_SYS, null, e, tag + "redis ladd error");
            throw new NLUnCheckedException(errorCodeOp, "redis publish error", e);
        }
    }
}
