package com.oax.vo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class AppRegister {
	@NotBlank
	private String username;
	@NotBlank
	@Size(min=6, max=16)
	private String password;
	@NotBlank
	@Size(min=6, max=16)
	private String repeatPassword;
	
	private String invateCode;
	@NotNull
	private Integer source;
	
	@NotNull
	private Integer type;

	@NotNull
	private String code;

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
