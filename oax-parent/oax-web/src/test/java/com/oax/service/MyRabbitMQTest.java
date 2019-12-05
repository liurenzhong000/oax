package com.oax.service;

import com.rabbitmq.client.*;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@SpringBootTest
public class MyRabbitMQTest {

    //获取接连
    public Connection getConnection() throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();

        //设置连接MQ的IP地址
        factory.setHost("47.106.117.226");
        //设置连接端口号
        factory.setPort(5672);
        //设置要接连MQ的库（域）
        factory.setVirtualHost("/");
        //连接帐号
        factory.setUsername("admi");
        //连接密码
        factory.setPassword("passworld");
        return factory.newConnection();
    }

    //定义队列名称
    public static String QUEUE_NAME = "test_work_queue";

    @Test
    public void testSender() throws IOException, TimeoutException {
        //获取连接
        Connection connection = getConnection();

        //创建channel通道
        Channel channel = connection.createChannel();

        //声明队列
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);

        //循环发送30条消息
        for (int i = 0; i < 3000 ; i++) {

            //声明消息
            String msg = "this is msg [" + i + "]";
            System.out.println("发送消息："+msg);
            //发送消息
            channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //关闭通道
        channel.close();
        //关闭连接
    }

    @Test
    public void testCustomer() throws IOException, TimeoutException {
        Connection connection = getConnection();
        Channel channel = connection.createChannel();
        channel.basicQos(1);
        Channel channel2 = connection.createChannel();
        channel2.basicQos(1);

        Channel channel3 = connection.createChannel();
        channel3.basicQos(1);

        //声明队列
//        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //定义消费者

        customer(channel,"消费者1号");
        customer(channel2, "消费者2号");
        customer(channel3, "消费者3号");
//        customer(channel, "消费者4号");
        try {
            Thread.sleep(100000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void customer(Channel channel, String name) throws IOException {
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                System.out.println("==========================");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //获取并转成String
                String message = new String(body, "UTF-8");
                System.out.println(name+ "-->收到消息,msg :"+message+"==="+Thread.currentThread().getName());
                channel.basicReject(envelope.getDeliveryTag(), false);
            }
        };
        //监听队列
        channel.basicConsume(QUEUE_NAME, false, consumer);
    }
}
