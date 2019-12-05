package com.oax;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@MapperScan(basePackages = {"com.oax.mapper.front", "com.oax.mapper.admin", "com.oax.mapper"})
@Slf4j
public class OaxBTCApplication {



    public static void main(String[] args) {
        SpringApplication.run(OaxBTCApplication.class, args);
    }
}
