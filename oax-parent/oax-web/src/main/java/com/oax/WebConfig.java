package com.oax;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.oax.filter.BaseFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
//@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private BaseFilter baseFilter;

    @Bean
	public FilterRegistrationBean loginFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(baseFilter);
		registration.addUrlPatterns("/*");
		registration.setName("loginFilter");
		registration.setOrder(1);
		return registration;
	}

	//mybatis plus 乐观锁插件
	@Bean
	public OptimisticLockerInterceptor optimisticLockerInterceptor() {
		return new OptimisticLockerInterceptor();
	}

}
