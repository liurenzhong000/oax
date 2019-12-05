package com.oax.vo;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class CheckPasswordVO {
	
	@NotBlank
	private String oldPassword;
	
	@NotBlank
	private String newPassword;

	@NotNull
	private Integer type;


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

	@NotNull
	public Integer getType() {
		return type;
	}

	public void setType(@NotNull Integer type) {
		this.type = type;
	}
}
