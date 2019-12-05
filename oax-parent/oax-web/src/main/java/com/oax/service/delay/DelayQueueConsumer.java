package com.oax.service.delay;

import com.oax.common.json.JsonHelper;
import com.oax.mq.MQConstant;
import com.oax.service.strategy.DelayConsumerStrategy;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * 当死信队列里的任务到期后，过期转发到这个队列，在这个队列处理所有的过期任务
 */
@Slf4j
@Component
@RabbitListener(queues = MQConstant.DEFAULT_DELAY_QUEUE)
public class DelayQueueConsumer {

    @Autowired
    private List<DelayConsumerStrategy> consumerStrategies;

    @RabbitHandler
    public void process(String queueDesc, Channel channel, Message message) throws IOException {
        //TODO 添加多线程处理， 添加消息确认机制，Object处理
        //此时，才把消息发送到指定队列，而实现延迟功能
        log.info("处理过期任务 - message queueDesc:{}", queueDesc);
        try {
            DelayQueueDesc delayQueueDesc = JsonHelper.readValue(queueDesc, DelayQueueDesc.class);
            for (DelayConsumerStrategy strategy : consumerStrategies) {
                if (strategy.accept(delayQueueDesc.getType())) {
                    strategy.handle(delayQueueDesc);
                    continue;
                }
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            log.info("处理过期任务成功 - queueName:{} - id:{}", delayQueueDesc.getType().queueName, delayQueueDesc.getId());
        } catch (Exception e) {
            log.error("处理过期任务失败："+ message, e);
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }
}
