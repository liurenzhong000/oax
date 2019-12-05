package com.oax.service;

import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.front.SmsCaptcha;
import com.oax.exception.VoException;

import javax.servlet.http.HttpServletRequest;

public interface SmsCaptchaService {

	void add(SmsCaptcha smsEntity);

	boolean checkSms(HttpServletRequest request,String phone,String sms,String lang) throws VoException;

	boolean checkSmsOnce(String phone, String sms) throws VoException;

	boolean checkNewlyCode(HttpServletRequest request, String phone, String sms, String lang) throws VoException;

	void resetExpireTime(String phone, String smsCode);

	void sendPwd(String phone,String pwd);

}