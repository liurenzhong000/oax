package com.oax.entity.front;

import java.math.BigDecimal;

/**
 * user_coin
 * @author 
 */
public class Property {
    private Integer id;
    private BigDecimal total;
    private BigDecimal banlance;
    private BigDecimal freezingBanlance;
    private BigDecimal btcPrice;
    private BigDecimal fee;
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public BigDecimal getBanlance() {
		return banlance;
	}
	public void setBanlance(BigDecimal banlance) {
		this.banlance = banlance;
	}
	public BigDecimal getFreezingBanlance() {
		return freezingBanlance;
	}
	public void setFreezingBanlance(BigDecimal freezingBanlance) {
		this.freezingBanlance = freezingBanlance;
	}
	public BigDecimal getBtcPrice() {
		return btcPrice;
	}
	public void setBtcPrice(BigDecimal btcPrice) {
		this.btcPrice = btcPrice;
	}
	public BigDecimal getFee() {
		return fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
    
}