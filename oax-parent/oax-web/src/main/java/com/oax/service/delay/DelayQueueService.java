package com.oax.service.delay;

import com.oax.common.json.JsonHelper;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 消息队列服务接口实现
 */
@Service
public class DelayQueueService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息到对应的死信队列
     */
    public void send(DelayQueueDesc delayQueueDesc) {
        //消息发送到死信队列上，当消息超时时，会发生到处理定时任务的队列上，定时到期队列收到到期的消息时进行过期业务处理
        DelayQueueType delayQueueType = delayQueueDesc.getType();
        MessagePostProcessor processor = message -> {
            message.getMessageProperties().setExpiration(delayQueueType.timeout + "");
            return message;
        };
        rabbitTemplate.convertAndSend(delayQueueDesc.getExchange(), delayQueueType.queueName, JsonHelper.writeValueAsString(delayQueueDesc), processor);
    }
}
