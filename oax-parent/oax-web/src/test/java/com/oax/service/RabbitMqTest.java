package com.oax.service;

import com.oax.OaxApiApplicationTest;
import com.oax.service.delay.DelayQueueDesc;
import com.oax.service.delay.DelayQueueService;
import com.oax.service.delay.DelayQueueType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqTest extends OaxApiApplicationTest {

    @Autowired
    private DelayQueueService delayQueueService;

    @Test
    public void delayQueueTest() {
        System.out.println("发送时间:"+ System.currentTimeMillis());
        DelayQueueDesc delayQueueDesc = DelayQueueDesc.newInstance(DelayQueueType.CTC_BUYORDER_TIMEOUT, 1+"", null);
        delayQueueService.send(delayQueueDesc);
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
