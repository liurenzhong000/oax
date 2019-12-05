package com.oax.vo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class PhoneRegister {
	@NotBlank
	private String phone;
	
	@Size(min=6, max=16)
	private String password;
	
	private String invateCode;
	@NotNull
	private Integer source;
	@Size(min=6, max=16)
	private String repeatPassword;

	@NotBlank
	private String smsCode;
	public String getInvateCode() {
		return invateCode;
	}

	public void setInvateCode(String invateCode) {
		this.invateCode = invateCode;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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
}
