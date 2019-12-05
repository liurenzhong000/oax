package com.oax.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/** 
* @ClassName:：OrdersVO 
* @Description： 当前订单入参
* @author ：xiangwh  
* @date ：2018年6月13日 下午5:09:49 
*  
*/
public class OrdersApiVO implements Serializable {
	
	private static final long serialVersionUID = 8687610414078561515L;
	private String apiKey;
	//交易对
	private String market;
	//买入还是卖出  1买入  2卖出
	private Integer type;	
	//价格
	private BigDecimal price;
	//数量
	private BigDecimal qty;
	
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public String getMarket() {
		return market;
	}
	public void setMarket(String market) {
		this.market = market;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getQty() {
		return qty;
	}
	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}
}
