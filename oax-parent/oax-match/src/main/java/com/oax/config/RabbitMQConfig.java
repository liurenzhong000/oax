package com.oax.config;



import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.oax.mq.MatchComsumer;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/** 
* @ClassName:：RabbitMQConfig 
* @Description： rabbitMQ配置
* @author ：xiangwh  
* @date ：2018年6月19日 下午4:38:37 
*  
*/
@Configuration
public class RabbitMQConfig {
	@Value("${spring.rabbitmq.host}")
	private String host;
	@Value("${spring.rabbitmq.port}")
	private int port;
	@Value("${spring.rabbitmq.username}")
	private String username;
	@Value("${spring.rabbitmq.password}")
	private String password;
	
	@Bean  
    public ConnectionFactory connectionFactory() {  
		ConnectionFactory factory = new ConnectionFactory();   
		factory.setHost(host);
		factory.setPort(port);
		factory.setUsername(username);  
		factory.setPassword(password);   
        return factory;  
    }
	

    @Bean
    public Connection initConnect(ConnectionFactory factory) throws IOException, TimeoutException {
    	Connection conn = factory.newConnection();
    	System.out.println("初始化mq链接::"+conn);
    	return conn;
    }
    
    @Bean
    public Channel getChanne(Connection conn) throws IOException {
    	Channel channel = conn.createChannel();
    	channel.queueDeclare(MatchComsumer.QUEUE_NAME, true, false, false, null);
    	channel.basicQos(1);
    	return channel;
    }

}
