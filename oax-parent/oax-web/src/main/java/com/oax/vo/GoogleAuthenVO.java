package com.oax.vo;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class GoogleAuthenVO {
	
	@NotBlank
	private String googleKey;
	
	@NotNull
	private Long googleCode;

	public String getGoogleKey() {
		return googleKey;
	}

	public void setGoogleKey(String googleKey) {
		this.googleKey = googleKey;
	}

	public Long getGoogleCode() {
		return googleCode;
	}

	public void setGoogleCode(Long googleCode) {
		this.googleCode = googleCode;
	}
	
}
