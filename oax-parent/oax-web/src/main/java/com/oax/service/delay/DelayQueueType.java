package com.oax.service.delay;

/**
 * 定时任务相关类型
 * 注意：由于rabbitmq队列时有序的，不要随意更改过期时间，因为有可能造成过期转发异常
 * 添加新的任务步骤：
 * 1.添加新的DelayQueueType枚举类型，定义好queueName，超时时间
 * 2.添加新的任务类型处理策略：继承DelayConsumerStrategy
 * 3.在对应的业务代码使用DelayQueueService发送消息
 */
public enum DelayQueueType {

    CTC_BUYORDER_TIMEOUT("web.ctc.buyOrder.timeout",60000 * 30L,"CTC用户买入未付款订单超时"),
    CTC_SALEORDER_TIMEOUT("web.ctc.saleOrder.timeout",60000 * 60 * 24L,"CTC用户卖出未付款订单超时");

    /**死信队列名称*/
    public String queueName;
    /**对应死信队列中消息的过期时间，同意*/
    public Long timeout;
    /**queue业务描述*/
    public String desc;

    DelayQueueType(String queueName, Long timeout, String desc) {
        this.queueName = queueName;
        this.timeout = timeout;
        this.desc = desc;
    }
}
