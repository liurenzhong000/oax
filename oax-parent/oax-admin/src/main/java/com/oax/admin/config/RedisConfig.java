package com.oax.admin.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/5/29
 * Time: 11:41
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.pool.max-wait}")
    private long maxWaitMillis;

//    @Value("${spring.redis.database}")
    private int database = 15;//给JedisPool的

    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public JedisPool redisPoolFactory() {
        Logger.getLogger(getClass()).info("JedisPool注入成功！！");
        Logger.getLogger(getClass()).info("redis地址：" + host + ":" + port);
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);

        JedisPool jedisPool;
        if (StringUtils.isEmpty(password)) {
            jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, null, database);
        } else {
            jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password, database);
        }
        return jedisPool;
    }
}
