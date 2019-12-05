package com.oax.vo;

import javax.validation.constraints.NotNull;

public class CloseCheckVO {
	
	private String emailCode;
	
	private String smsCode;

	private Long googleCode;
	
	@NotNull
	private Integer type;//1 手机  2邮箱  3google
	
	@NotNull
	private Integer status;//0 关闭 1开启
	
	public String getEmailCode() {
		return emailCode;
	}

	public void setEmailCode(String emailCode) {
		this.emailCode = emailCode;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public Long getGoogleCode() {
		return googleCode;
	}

	public void setGoogleCode(Long googleCode) {
		this.googleCode = googleCode;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
