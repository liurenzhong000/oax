package com.oax.job;

import javax.annotation.PostConstruct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.mq.MatchComsumer;

import java.io.IOException;

/** 
* @ClassName:：ConsumerJob 
* @Description： 
* @author ：xiangwh  
* @date ：2018年6月25日 下午7:09:54 
*  
*/
@Service
public class OrdersConsumerJob {

	@Autowired
	private Connection connection;

	@Autowired
	private MatchComsumer comsumer;

	@Autowired
	private Channel channel;
	
	@PostConstruct
	public void consumer() throws Exception {
		comsumer.work(getChannel());
		comsumer.work(getChannel());
		comsumer.work(getChannel());
		comsumer.work(getChannel());

//		comsumer.work(getChannel());
//		comsumer.work(getChannel());
//		comsumer.work(getChannel());
//		comsumer.work(getChannel());
	}

	private Channel getChannel() throws IOException {
		Channel channel = connection.createChannel();
		channel.queueDeclare(MatchComsumer.QUEUE_NAME, true, false, false, null);
		channel.basicQos(1);
		return channel;
	}
}
