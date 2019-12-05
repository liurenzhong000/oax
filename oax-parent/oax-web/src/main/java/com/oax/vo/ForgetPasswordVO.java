package com.oax.vo;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class ForgetPasswordVO {

	private String username;
	
	@NotBlank
	private String password;
	
	@NotBlank
	private String repeatPassword;

	@NotNull
	private Integer type;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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


	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
