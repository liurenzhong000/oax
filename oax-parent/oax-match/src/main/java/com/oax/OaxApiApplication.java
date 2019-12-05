package com.oax;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@ServletComponentScan
public class OaxApiApplication {
	

	public static void main(String[] args) throws Exception {
		SpringApplication.run(OaxApiApplication.class, args);
	}
	
}
