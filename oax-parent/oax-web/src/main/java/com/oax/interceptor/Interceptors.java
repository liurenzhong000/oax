package com.oax.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class Interceptors extends WebMvcConfigurerAdapter {

	@Bean
	public LoginInterceptor LoginInterceptor() {
		return new LoginInterceptor();
	}
	
	@Bean
	public ApiInterceptor apiInterceptor() {
		return new ApiInterceptor();
	}

	@Bean
	public ApiThirdInterceptor apiThirdInterceptor() {
		return new ApiThirdInterceptor();
	}

	@Bean
	public OrderInterceptor orderInterceptor(){
		return new OrderInterceptor();
	}

	@Bean
	public AccessLimitInterceptor accessLimitInterceptor(){return new AccessLimitInterceptor();}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(LoginInterceptor())
				.addPathPatterns("/**")
				.excludePathPatterns("/")
				.excludePathPatterns("/druid/*")
				.excludePathPatterns("/static/*")
				.excludePathPatterns("/user/emailRegister")
				.excludePathPatterns("/user/phoneRegister")
				.excludePathPatterns("/user/registerApp")
				.excludePathPatterns("/user/forgetPasswordPhone")
				.excludePathPatterns("/user/forgetPasswordEmail")
				.excludePathPatterns("/user/forgetPasswordApp")
				.excludePathPatterns("/user/forgetPasswordEmailUserUrl")
				.excludePathPatterns("/user/login")
				.excludePathPatterns("/user/checkLoginPassword")
				.excludePathPatterns("/user/checkEmail")
				.excludePathPatterns("/user/checkPhone/*")
				.excludePathPatterns("/user/queryCheckType")
				.excludePathPatterns("/userCoin/list")
				.excludePathPatterns("/helpCenter/**")
				.excludePathPatterns("/noticeCenter/**")
				.excludePathPatterns("/sms/**")
				.excludePathPatterns("/email/**")
				.excludePathPatterns("/countryCode/*")
				.excludePathPatterns("/tradeCoin/findAll")
				.excludePathPatterns("/trade/tradeListByMarketId")
				.excludePathPatterns("/marketCategory/selectAllBySort")
				.excludePathPatterns("/kline/findListByMarketId")
				.excludePathPatterns("/kline/findListByMarketIdNew")
				.excludePathPatterns("/banner/list/*")
				.excludePathPatterns("/tradeCoin/indexPageMarket")
				.excludePathPatterns("/app/*")
				.excludePathPatterns("/api/**")
				.excludePathPatterns("/thirdApi/**")
				.excludePathPatterns("/indexPage")
				.excludePathPatterns("/transactionPage/index")
				.excludePathPatterns("/transactionPage/indexes")
				.excludePathPatterns("/market/*")
				.excludePathPatterns("/user/checkForgetPassword")
				.excludePathPatterns("/redPacket/init/*")
				.excludePathPatterns("/redPacket/grab")
				.excludePathPatterns("/feedBack/*")
				.excludePathPatterns("/lockPosition/index")
                .excludePathPatterns("/lockPosition/initLockPositionInfo")
				.excludePathPatterns("/lockPosition/index/list")
				.excludePathPatterns("/tradeRedman/index")
				.excludePathPatterns("/lockPosition/lockCoin/init")
				.excludePathPatterns("/shareBonus/index")
				.excludePathPatterns("/util/**")
				.excludePathPatterns("/ctcAdvert/info")
				.excludePathPatterns("/robot/**")

				.excludePathPatterns("/**/anon")//anon表示不用拦截


				.excludePathPatterns("/swagger-resources/**","/webjars/**", "/v2/**", "/swagger-ui.html");

		registry.addInterceptor(apiThirdInterceptor())
				.addPathPatterns("/api/third/*");

		registry.addInterceptor(apiInterceptor())
				.addPathPatterns("/api/user/*");

		registry.addInterceptor(orderInterceptor())
				.addPathPatterns("/orders/add")
				.addPathPatterns("/orders/cancel/*");

		registry.addInterceptor(accessLimitInterceptor());
	}
}
