package com.oax.vo;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class WithdrawalVO {
	
	@NotBlank
	private String address;
	
	private String remark;
	
	@NotNull
	private BigDecimal qty;
	
	@NotNull
	private Integer coinId;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public Integer getCoinId() {
		return coinId;
	}

	public void setCoinId(Integer coinId) {
		this.coinId = coinId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
}
