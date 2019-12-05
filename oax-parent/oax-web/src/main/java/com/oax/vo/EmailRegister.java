package com.oax.vo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class EmailRegister {
	@NotBlank
	@Email
	private String email;
	@NotBlank
	@Size(min=6, max=16)
	private String password;
	@NotBlank
	@Size(min=6, max=16)
	private String repeatPassword;
	
	private String invateCode;
	@NotNull
	private Integer source;
	
	private String emailCode;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRepeatPassword() {
		return repeatPassword;
	}

	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}

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

	public String getEmailCode() {
		return emailCode;
	}

	public void setEmailCode(String emailCode) {
		this.emailCode = emailCode;
	}
}
