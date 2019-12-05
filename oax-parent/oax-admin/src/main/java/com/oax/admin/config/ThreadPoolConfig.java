package com.oax.admin.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/** 
* @ClassName:：ThreadPoolConfig 
* @Description： 线程池配置类
* @author ：xiangwh  
* @date ：2018年6月12日 上午9:09:02 
*  
*/
@Configuration
@EnableAsync
public class ThreadPoolConfig {
	 	@Bean(name="threadPoolTaskExecutor")
	    public ThreadPoolTaskExecutor myExecutor() {
	        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	        executor.setCorePoolSize(5);
	        executor.setMaxPoolSize(50);
	        executor.setQueueCapacity(100);
	        executor.setKeepAliveSeconds(200);
	        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
	        executor.initialize();
	        return executor;
	    }
}
