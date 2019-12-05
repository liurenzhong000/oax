package com.oax.admin.controller;

import java.math.BigDecimal;

/** 
* @ClassName:：RechargeInfo 
* @Description： ceshi
* @author ：xiangwh  
* @date ：2018年7月12日 下午9:42:23 
*  
*/
public class RechargeInfo {
	private Integer coinType; 
	private String address; 
	private BigDecimal qty;
	 
	public Integer getCoinType() {
		return coinType;
	}

	public void setCoinType(Integer coinType) {
		this.coinType = coinType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	@Override
	public String toString() {
		return "RechargeInfo [coinType=" + coinType + ", address=" + address + ", qty=" + qty + "]";
	} 
	
	
}
