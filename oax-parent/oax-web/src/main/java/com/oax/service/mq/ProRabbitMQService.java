package com.oax.service.mq;


import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.oax.vo.OrdersMqVO;

/** 
* @ClassName:：RabbitMQServiceImpl 
* @Description： rabbitMQService
* @author ：xiangwh  
* @date ：2018年6月19日 下午5:47:43 
*  
*/
@Component
public class ProRabbitMQService{
	@Autowired
    private AmqpTemplate rabbitTemplate;

    public void sendOrder(OrdersMqVO mqVO) {
        this.rabbitTemplate.convertAndSend("orderQueue", JSON.toJSONString(mqVO));
    }

}
