package com.oax.vo;

import org.hibernate.validator.constraints.NotBlank;

public class BindPhoneVO {
	
	@NotBlank
	private String phone;
	
	private String code;
	
	private String emailCode;
	
	private Long googleCode;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getEmailCode() {
		return emailCode;
	}

	public void setEmailCode(String emailCode) {
		this.emailCode = emailCode;
	}

	public Long getGoogleCode() {
		return googleCode;
	}

	public void setGoogleCode(Long googleCode) {
		this.googleCode = googleCode;
	}

}
