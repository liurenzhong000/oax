package com.oax.vo;

import org.hibernate.validator.constraints.NotBlank;

public class TransactionPasswordVO {
	
	@NotBlank
	private String transactionPassword;
	
	@NotBlank
	private String repeatPassword;
	

	public String getTransactionPassword() {
		return transactionPassword;
	}

	public void setTransactionPassword(String transactionPassword) {
		this.transactionPassword = transactionPassword;
	}

	public String getRepeatPassword() {
		return repeatPassword;
	}

	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}

	
}
