package com.oax.service.strategy;

import com.oax.service.delay.DelayQueueDesc;
import com.oax.service.delay.DelayQueueType;

/**
 * 定时任务消费端出来策略
 */
public interface DelayConsumerStrategy{

    boolean accept(DelayQueueType type);

    void handle(DelayQueueDesc item);

}
