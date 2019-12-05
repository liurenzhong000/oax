package com.oax.vo;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class ForgetPasswordEmailVO {
	
	@NotBlank
	@Email
	private String email;
	
	@NotBlank
	private String password;
	
	@NotBlank
	private String repeatPassword;
	
	private String emailCode;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailCode() {
		return emailCode;
	}

	public void setEmailCode(String emailCode) {
		this.emailCode = emailCode;
	}
}
