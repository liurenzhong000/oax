package com.oax.service.delay;

import com.oax.mq.MQConstant;
import lombok.Data;

import java.io.Serializable;

/**
 * 定时队列里存放消息的内容，用来处理业务逻辑
 */
@Data
public class DelayQueueDesc implements Serializable {

    /**
     * 队列类型：CTC未付款订单超时(指定发送到哪个死信队列)queueName
     */
    private DelayQueueType type;

    /**
     * 主体id：订单id，等
     */
    private String id;

    /**
     * 附带的其他内容
     */
    private Object extra;

    /**
     * exchange
     */
    private String exchange;

    private DelayQueueDesc() {

    }

    public static final DelayQueueDesc newInstance(DelayQueueType type,String id,Object extra){
        DelayQueueDesc delayQueueDesc=new DelayQueueDesc();
        delayQueueDesc.setType(type);
        delayQueueDesc.setId(id);
        delayQueueDesc.setExtra(extra);
        delayQueueDesc.setExchange(MQConstant.DEFAULT_EXCHANGE);
        return delayQueueDesc;
    }

    public static final DelayQueueDesc newInstance(DelayQueueType type,String id){
        DelayQueueDesc delayQueueDesc=new DelayQueueDesc();
        delayQueueDesc.setType(type);
        delayQueueDesc.setId(id);
        delayQueueDesc.setExchange(MQConstant.DEFAULT_EXCHANGE);
        return delayQueueDesc;
    }
}
