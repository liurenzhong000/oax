package com.oax.vo;

public class OpenTradePasswordVO {
	
	private String emailCode;
	
	private String smsCode;

	private Long googleCode;
	
	private Integer flag;

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

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
}
