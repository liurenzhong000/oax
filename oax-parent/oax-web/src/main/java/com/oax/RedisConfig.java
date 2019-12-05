package com.oax;

import com.oax.constant.WebSocketKeyConstant;
import com.oax.service.common.RedisReceiver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Auther: hyp
 * @Date: 2018/12/26 15:35
 * @Description:
 */
@Configuration
@Slf4j
public class RedisConfig {

    @Autowired
    JedisConnectionFactory jedisConnectionFactory;

    @Bean
    public RedisTemplate<String, Object> commonRedisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public RedisTemplate<String, Integer> redisTemplate() {
        RedisTemplate<String, Integer> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericToStringSerializer<>(Integer.class));
        return template;
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory jedisConnectionFactory,
                                                        MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic(WebSocketKeyConstant.SNATCH_BET_MSG));//channel名称 //配置redis发布订阅相关
        container.addMessageListener(listenerAdapter, new PatternTopic(WebSocketKeyConstant.SNATCH_ACTIVITY_MSG));//channel名称 //配置redis发布订阅相关
        container.addMessageListener(listenerAdapter, new PatternTopic(WebSocketKeyConstant.SNATCH_LOTTERY_BROADCAST_MSG));
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(RedisReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }


}
