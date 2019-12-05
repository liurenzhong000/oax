package com.oax;

public class Constant {
	
	public static final String api_header_accessToken ="accessToken";
	
	public static final String api_header_lang ="lang";
	
	public static final String api_header_userId ="userId";
	
	public static final long reqs_outtime =1000*60*5;
	
	public static final long REDISTIME_ONEDAY = 60*60*24;	
	public static final long REDISTIME_ANHOUR = 60*60;	
	
	//用户登陆一天内允许的最大密码错误次数
	public static final Integer LONGIN_COUNT_LIMIT =5;	
	
	//验证码过期时间
	public static final long CODE_OUT_LIMIT1 =1000*60*5;	
	public static final long CODE_OUT_LIMIT2 =1000*60*30;	

}
