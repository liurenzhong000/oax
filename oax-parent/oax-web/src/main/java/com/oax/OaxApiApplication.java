package com.oax;

import javax.servlet.MultipartConfigElement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ServletComponentScan
@EnableScheduling
@EnableAsync
@MapperScan(basePackages = {"com.oax.mapper.front", "com.oax.mapper.admin", "com.oax.mapper"})
@EnableTransactionManagement//开启事务
public class OaxApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OaxApiApplication.class, args);
	}
	
	@Bean  
    public MultipartConfigElement multipartConfigElement() {  
        MultipartConfigFactory factory = new MultipartConfigFactory();  
        //单个文件最大  
        factory.setMaxFileSize("5120KB"); //KB,MB
        /// 设置总上传数据总大小  
        factory.setMaxRequestSize("20480KB");  
        return factory.createMultipartConfig();  
    }  
}
