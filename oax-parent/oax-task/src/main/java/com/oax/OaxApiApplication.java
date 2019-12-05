package com.oax;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@MapperScan(basePackages = {"com.oax.mapper.front", "com.oax.mapper.admin", "com.oax.mapper"})
public class OaxApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OaxApiApplication.class, args);
	}
}
