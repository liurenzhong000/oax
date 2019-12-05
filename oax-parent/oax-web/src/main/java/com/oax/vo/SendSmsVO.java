package com.oax.vo;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class SendSmsVO {
	
	@NotBlank
	private String phone;
	
	@NotNull
	private Integer type;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}


}
