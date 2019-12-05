package com.oax.mq;

import com.oax.service.delay.DelayQueueDesc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

/** 
* @ClassName:：RabbitMQConfig 
* @Description： rabbitMQ配置
* @author ：xiangwh  
* @date ：2018年6月19日 下午4:38:37 
*  
*/
@Configuration
@Slf4j
//@PropertySource(value = "classpath:/rabbitmq.properties")
public class RabbitMQConfig {
	@Value("${spring.rabbitmq.host}")
	private String host;
	@Value("${spring.rabbitmq.port}")
	private int port;
	@Value("${spring.rabbitmq.username}")
	private String username;
	@Value("${spring.rabbitmq.password}")
	private String password;
	@Value("${spring.rabbitmq.publisher-confirms}")
	private boolean publisherConfirms;
	@Value("${spring.rabbitmq.virtual-host}")
	private String virtualHost;
	
	@Bean  
    public ConnectionFactory connectionFactory() {  
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);   
        connectionFactory.setUsername(username);  
        connectionFactory.setPassword(password);  
        connectionFactory.setVirtualHost(virtualHost);  
        connectionFactory.setPublisherConfirms(publisherConfirms);//消息确认  
        connectionFactory.setPublisherReturns(true);
        log.info("Create RabbitMQ ConnectionFactory bean..");
        return connectionFactory;  
    }
	
	//rabbitmq的模板配置
    @Bean
//  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) //必须是prototype类型
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(this.connectionFactory());
//        template.setConfirmCallback();// 设置消息确认
//        template.setReturnCallback();
        return template;
    }

}
