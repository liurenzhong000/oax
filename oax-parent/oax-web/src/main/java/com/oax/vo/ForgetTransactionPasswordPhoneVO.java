package com.oax.vo;

import org.hibernate.validator.constraints.NotBlank;

public class ForgetTransactionPasswordPhoneVO {
	@NotBlank
	private String password;
	
	@NotBlank
	private String repeatPassword;
	
	@NotBlank
	private String checkCode;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRepeatPassword() {
		return repeatPassword;
	}

	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

}
