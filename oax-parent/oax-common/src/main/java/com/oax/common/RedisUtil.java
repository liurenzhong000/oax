package com.oax.common;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.annotation.Resource;

@Component
public class RedisUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, Integer> intOps;
    //默认失效时间
    public final static long DEFAULT_TIMEOUT =1 * 60 * 60 ;
    public final static long NOT_TIMEOUT = -1;
    private final static String LOCK_PREFIX = "lock:";

    public StringRedisTemplate getStringRedisTemplate(){
        return stringRedisTemplate;
    }

    public Integer getStringToInteger(String key) {
        String value = stringRedisTemplate.opsForValue().get(key);
        if (value == null || value.equals("")) {
            return 0;
        }
        return Integer.valueOf(value);
    }

    public void setString(String key, String value) {
        setString(key, value, DEFAULT_TIMEOUT);
    }

    public void setString(String key, String value, long timeout) {

        stringRedisTemplate.opsForValue().set(key, value);

        if (timeout != NOT_TIMEOUT) {
            stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        }
    }

    public String getString(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void setObject(String key, Object value) {
        setObject(key, value, DEFAULT_TIMEOUT);
    }

    public void setObject(String key, Object value, long timeout) {

        stringRedisTemplate.opsForValue().set(key, JSONObject.toJSONString(value));

        if (timeout != NOT_TIMEOUT) {
            stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        }
    }

    public <T> T getObject(String key, Class<T> clazz) {
        String value = stringRedisTemplate.opsForValue().get(key);

        return value == null ? null : JSON.parseObject(value, clazz);
    }

    public void setList(String key, List<?> value) {
        setList(key, value, DEFAULT_TIMEOUT);
    }

    public void setList(String key, List<?> value, long timeout) {

        stringRedisTemplate.opsForValue().set(key, JSONArray.toJSONString(value));

        if (timeout != NOT_TIMEOUT) {
            stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        }
    }

    public <T> List<T> getList(String key, Class<T> clazz) {
        String value = stringRedisTemplate.opsForValue().get(key);

        return value == null ? null : JSON.parseArray(value, clazz);
    }

    public boolean expire(String key) {
        return stringRedisTemplate.expire(key, DEFAULT_TIMEOUT, TimeUnit.SECONDS);
    }

    public boolean expire(String key, long timeout) {
        return stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    public void deleteKeys(String keys) {

        Set<String> keySet = stringRedisTemplate.keys(keys);
        stringRedisTemplate.delete(keySet);
    }

    public Long setSize(String key) {
        return stringRedisTemplate.opsForSet().size(key);
    }

    public void putToSet(String key, String value) {
        stringRedisTemplate.opsForSet().add(key, value);
    }

    public boolean existsInSet(String key, String value) {
        return stringRedisTemplate.opsForSet().isMember(key, value);
    }

    public Integer incrAndGet(String key, int timeout) throws IllegalAccessException {
        boolean locked = tryLock(key, 2);
        try {
            if (locked) {
                Integer val = intOps.get(key);
                if (val == null) {
                    val = 0;
                }
                intOps.set(key, ++val, timeout, TimeUnit.SECONDS);
                return val;
            }
            throw new IllegalAccessException("key已经被锁住");
        } finally {
            unLock(key);
        }
    }

    public Integer increAndGet(String key, int increment) {
        boolean locked = tryLock(key, 2);
        try {
            if (locked) {
                Integer val = intOps.get(key);
                val = val == null ? 0 : val;
                val = val + increment;
                intOps.set(key, val);
                return val;
            }
            throw new IllegalArgumentException("key已经被锁住");
        } finally {
            unLock(key);
        }
    }

    public Integer incrAndGet(String key) throws IllegalAccessException {
        boolean locked = tryLock(key, 2);
        try {
            if (locked) {
                Integer val = intOps.get(key);
                val = val == null ? 0 : val;
                intOps.set(key, ++val);
                return val;
            }
            throw new IllegalAccessException("key已经被锁住");
        } finally {
            unLock(key);
        }

    }

    public Integer reduceAndGet(String key) throws IllegalAccessException {
        boolean locked = tryLock(key, 2);
        try {
            if (locked) {
                Integer val = intOps.get(key);
                if (val == null) {
                    return 0;
                }
                intOps.set(key, --val);
                return val;
            }
            throw new IllegalAccessException("key已经被锁住");
        } finally {
            unLock(key);
        }
    }

    public Integer reduceAndGet(String key, int timeout) throws IllegalAccessException {
        boolean locked = tryLock(key, 2);
        try {
            if (locked) {
                Integer val = intOps.get(key);
                if (val == null) {
                    return 0;
                }
                intOps.set(key, --val, timeout, TimeUnit.SECONDS);
                return val;
            }
            throw new IllegalAccessException("key已经被锁住");
        } finally {
            unLock(key);
        }
    }

    /**
     * 默认的锁数据过期时间
     */
    private final int DEFAULT_KEY_TIMEOUT_S = 10;

    public boolean tryLock(String key) {
        return tryLock(key, DEFAULT_KEY_TIMEOUT_S);
    }

    public boolean tryLock(String key, Integer seconds) {
        String lockKey = getLockKey(key);
        Boolean success = stringRedisTemplate.execute(connection -> {
            Boolean setSucc = connection.setNX(lockKey.getBytes(), "".getBytes());
            if (setSucc) {
                connection.expire(lockKey.getBytes(), seconds);
            }
            return setSucc;
        }, true);
        return success;
    }

    /**判断锁是否存在*/
    public boolean isLock(String key){
        String lockKey = getLockKey(key);
        String value = getString(lockKey);
        if (value != null) {
            return true;
        }
        return false;
    }

    public void unLock(String key) {
        stringRedisTemplate.delete(getLockKey(key));
    }

    private String getLockKey(String key) {
        return LOCK_PREFIX + key;
    }

    //========================zset相关======================================
    public void zincrement(String key, String member) {
        zincrement(key, member, 1);
    }

    public void zreduce(String key, String member) {
        zincrement(key, member, -1);
    }

    //zset的score添加 score
    public void zincrement(String key, String member, double score) {
        stringRedisTemplate.opsForZSet().incrementScore(key, member, score);
    }

    //设置zset的score的分数是多少
    public void zadd(String key, String member, double score) {
        stringRedisTemplate.opsForZSet().add(key, member, score);
    }

    public Long zsize(String key) {
        return stringRedisTemplate.opsForZSet().size(key);
    }

    public Double getZSetScore(String key, String value) {
        return stringRedisTemplate.opsForZSet().score(key, value);
    }

    public Long delFromZSet(String key, String... values) {
        return stringRedisTemplate.opsForZSet().remove(key, values);
    }

    public Set<String> zrangeAll(String key) {
        return stringRedisTemplate.opsForZSet().range(key, 0, -1);
    }

    public Set<String> zrange(String key, long start, long end) {
        return stringRedisTemplate.opsForZSet().range(key, start, end);
    }

    public Set<String> reverseRange(String key, long start, long end) {
        return stringRedisTemplate.opsForZSet().reverseRange(key, start, end);
    }

}
