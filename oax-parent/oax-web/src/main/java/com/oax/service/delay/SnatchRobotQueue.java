package com.oax.service.delay;

import com.oax.service.activity.SnatchActivityService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Auther: hyp
 * @Date: 2019/1/24 11:14
 * @Description: 延时队列处理对象 延迟添加机器人
 */
@Slf4j
public class SnatchRobotQueue implements Runnable {

    /** 延时队列 */
    private static DelayQueue<SnatchRobotDelay> queue = new DelayQueue<SnatchRobotDelay>();

    /** 原子对象解决内存可见性问题,避免资源消耗     */
    private static AtomicBoolean isRun = new AtomicBoolean();

    /** 处理线程池 */
    private static ExecutorService executorService =  Executors.newSingleThreadExecutor();

    /** 添加机器人延迟豪秒数 */
    public static final int MIN_ADD_ROBOT_MILLISECOND = 3000;
    public static final int MAX_ADD_ROBOT_MILLISECOND = 12000;

    /**
     * put需要延迟检测的数据（添加机器人）
     */
    public static void put(Integer snatchActivityId, SnatchActivityService snatchActivityService) {
        Date delay = new Date();
        delay.setTime(delay.getTime() + RandomUtils.nextInt(MIN_ADD_ROBOT_MILLISECOND, MAX_ADD_ROBOT_MILLISECOND));
        SnatchRobotDelay delayEvent = new SnatchRobotDelay(delay, snatchActivityId,snatchActivityService);

//        log.info("snatchActivityId:{},delayTime:{}",snatchActivityId, delay);

        queue.add(delayEvent);
        if(queue.size() > 0 && !isRun.get()) {
            executorService.execute(new SnatchRobotQueue());
        }
    }

    /**
     * 异步检测结果，每次只能一个线程处理该任务，通过延时阻塞(最佳方式采用协程处理)
     * 1、通过isRun原子对象解决并发问题
     * 2、获取延时队列数据，如果未到时间会阻塞
     * 3、机器人调用投注
     */
    public void run() {
        while(queue.size()>0) {
            try {
                /** 1、通过isRun原子对象解决并发问题,当没数据时关闭线程 *//*
	    		if(queue.size()==0 ) {
	    			isRun.set(false);
	    			executorService.shutdown();
	    			break;
	    		}
	    		isRun.set(true);*/

                /** 2、获取延时队列数据，如果未到时间会阻塞 */
                SnatchRobotDelay snatchRobotDelay = queue.take();
                if(snatchRobotDelay == null) {
                    continue;
                }

                //机器人id调用下单
                snatchRobotDelay.getSnatchActivityService().robotBet(snatchRobotDelay.getSnatchActivityId());

            }catch(InterruptedException e) {
                log.error(e.getMessage());
            }
        }
    }
}
