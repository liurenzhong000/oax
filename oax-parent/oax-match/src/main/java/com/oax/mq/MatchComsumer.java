package com.oax.mq;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.oax.entity.front.Orders;
import com.oax.entity.front.SysConfig;
import com.oax.mapper.front.OrdersMapper;
import com.oax.service.SysConfigService;
import com.oax.vo.OrdersMqVO;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

@Component
public class MatchComsumer {
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private OrdersMapper ordersMapper;
	@Autowired
	private MatchService matchService;
	@Autowired
	private SysConfigService sysConfigService;

	public final static String QUEUE_NAME = "orderQueue";

	public void work(Channel channel) throws Exception {

		System.out.println(Thread.currentThread().getName()+" 已连接正在等待消息...");

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException{
				System.out.println("线程"+Thread.currentThread().getName()+"   通道："+channel);
				String key = null;
				try {
					String  msg = new String(body, "UTF-8");
					System.out.println(Thread.currentThread().getName()+" 接收到消息："+ msg);

					OrdersMqVO ordersMqVO = JSON.parseObject(msg, OrdersMqVO.class);
					key = "lock."+ordersMqVO.getMarketId();
					boolean flag = false;

					//尝试加锁
				do {

//					System.out.println(Thread.currentThread().getName()+" 尝试加redis锁");
					flag=stringRedisTemplate.opsForValue().setIfAbsent(key, Thread.currentThread().getName());

				}while(!flag);
//					flag=stringRedisTemplate.opsForValue().setIfAbsent(key, Thread.currentThread().getName());
//					if (!flag){
//						System.out.println("============================================="+Thread.currentThread().getName()+" 获取锁失败 ="+key);
//						channel.basicReject(envelope.getDeliveryTag(), true);
//						return;
//					}

					stringRedisTemplate.expire(key, 3600, TimeUnit.SECONDS);
					System.out.println(Thread.currentThread().getName()+" 获取redis锁成功");

					Integer indexOrderId=ordersMqVO.getId();
					System.out.println(Thread.currentThread().getName()+" 正在处理订单ID="+indexOrderId);

					//修改订单状态由已下单改为待撮合 如果是0改1
					ordersMapper.upStatus(indexOrderId);

					//查询正在处理的订单信息
					Orders indexOrder = ordersMapper.getOrderById(indexOrderId);
					System.out.println(Thread.currentThread().getName()+" 正在处理订单信息="+JSON.toJSONString(indexOrder));
					SysConfig sysConfig = sysConfigService.marketFeeRate();
					BigDecimal tradeFeeRate=new BigDecimal(sysConfig.getValue());


					//正在处理的订单为买 并且状态是非撤销状态
					if(indexOrder.getType()==1&&indexOrder.getStatus()!=-1) {

						while(indexOrder.getStatus()==1||indexOrder.getStatus()==2) {

							boolean needContinue=matchService.matchByBuyOrder(indexOrder, tradeFeeRate);
							if(!needContinue) {
								break;
							}

							indexOrder = ordersMapper.getOrderById(indexOrderId);
						}


					}else if(indexOrder.getType()==2&&indexOrder.getStatus()!=-1) {

						//如果正在处理的卖单价格 小于 买1单
						while(indexOrder.getStatus()==1||indexOrder.getStatus()==2) {

							boolean needContinue=matchService.matchBySellOrder(indexOrder, tradeFeeRate);
							if(!needContinue) {
								break;
							}

							indexOrder = ordersMapper.getOrderById(indexOrderId);
						}

					}



					//通知已处理完成
					channel.basicAck(envelope.getDeliveryTag(), false);
				} catch (Exception e) {
					channel.basicReject(envelope.getDeliveryTag(), true);
					System.out.println("异常消息返回队列，"+Thread.currentThread());
					e.printStackTrace();
				} finally {
					//释放锁
					if (key!=null) {
						stringRedisTemplate.delete(key);
						System.out.println(Thread.currentThread().getName()+" 处理完成释放锁成功");
					}
				}

			}
		};
		channel.basicConsume(QUEUE_NAME, false, consumer);

	}


}