package com.oax.service;

import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.front.EmailCaptcha;
import com.oax.exception.VoException;

public interface EmailCaptchaService {

	void add(EmailCaptcha emailEntity);

	boolean checkEmail(String emailCode);

	String selectEmailByCode(String emailCode);

	boolean checkEmailCode(String emailCode, String email)throws VoException;

	boolean checkEmailCodeOnce(String emailCode, String email)throws VoException;

	boolean checkNewlyCode(String emailCode, String email)throws VoException;

    String sendRegisterUrl(String email, String emailCode, String lang);

	String checkForgetPasswordUrl(String checkCode);

	String sendForgetPasswordUrl(String email,String emailCode, String lang);

	String sendIpChangeEmail(String email, String lang);

	String sendEmailCode(String email, String emailCode, String lang);

	void resetExpireTime(String email,String emailCode);

	String sendEmailPwd(String email, String emailMsg);

	String sendEmailMsg(String email, String message, String lang);
}