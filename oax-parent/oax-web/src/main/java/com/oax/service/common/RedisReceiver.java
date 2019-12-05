package com.oax.service.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * @Auther: hyp
 * @Date: 2019/1/22 15:38
 * @Description: redis订阅发布，接受消息的处理
 */
@Component
@Slf4j
public class RedisReceiver {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void receiveMessage(String message, String channelName) {
        messagingTemplate.convertAndSend(channelName, message);
    }
}
