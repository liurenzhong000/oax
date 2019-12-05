package com.oax.vo;

import org.hibernate.validator.constraints.NotBlank;

public class UpdateLoginPasswordVO {
	
	@NotBlank
	private String oldPassword;
	
	@NotBlank
	private String newPassword;
	
	@NotBlank
	private String repeatPassword;
	
	private String smsCode;
	
	private String emailCode;
	
	private Long googleCode;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getRepeatPassword() {
		return repeatPassword;
	}

	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
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
