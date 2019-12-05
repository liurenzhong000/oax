package com.oax.vo;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class IdentityAuthenVO {
	
	@NotBlank
	private String idName;
	
	@NotNull
	private Integer cardType;
	
	@NotBlank
	private String  cardNo;

	@NotBlank
	private String  idImageA;
	
	@NotBlank
	private String  idImageB;
	
	@NotBlank
	private String  country;

	public String getIdName() {
		return idName;
	}

	public void setIdName(String idName) {
		this.idName = idName;
	}

	public Integer getCardType() {
		return cardType;
	}

	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getIdImageA() {
		return idImageA;
	}

	public void setIdImageA(String idImageA) {
		this.idImageA = idImageA;
	}

	public String getIdImageB() {
		return idImageB;
	}

	public void setIdImageB(String idImageB) {
		this.idImageB = idImageB;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}
