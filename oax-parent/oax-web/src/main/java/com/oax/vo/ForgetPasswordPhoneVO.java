package com.oax.vo;

import org.hibernate.validator.constraints.NotBlank;

public class ForgetPasswordPhoneVO {
	
	@NotBlank
	private String phone;
	
	@NotBlank
	private String password;
	
	@NotBlank
	private String repeatPassword;
	
	@NotBlank
	private String code;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
