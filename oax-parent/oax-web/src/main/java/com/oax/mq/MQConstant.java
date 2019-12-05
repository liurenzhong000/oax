package com.oax.mq;

/**
 * Rabbit消息队列相关常量
 *
 * * 定时任务相关队列在枚举DelayQueueType里
 * @see com.oax.service.delay.DelayQueueType
 */
public final class MQConstant {

    private MQConstant(){}

    //exchange name
    public static final String DEFAULT_EXCHANGE = "panduola.web";

    //DLX repeat QUEUE 死信队列过期后会被转发到这个队列，队列过期处理
    public static final String DEFAULT_DELAY_QUEUE = "web.delay.queue";

}
