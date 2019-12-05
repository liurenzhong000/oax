package com.oax.vo;

import java.io.Serializable;

/** 
* @ClassName:：OrdersVO 
* @Description： 当前订单入参
* @author ：xiangwh  
* @date ：2018年6月13日 下午5:09:49 
*  
*/
public class OrdersVO implements Serializable {
	
	private static final long serialVersionUID = 8687610414078561515L;
	//交易对id
	private Integer marketId;
	//买入还是卖出  1买入  2卖出
	private Integer type;	
	//价格
//	@NotBlank(message="价格不能为空")
//	@Pattern(regexp="^([1-9]\\d*(\\.\\d*[0-9])?)|(0\\.\\d*[0-9])$",message="价格不合法")
	private String price;
	//数量
//	@NotBlank(message="数量不能为空")
//	@Pattern(regexp="^([1-9]\\d*(\\.\\d*[0-9])?)|(0\\.\\d*[0-9])$",message="数量不合法")
	private String qty;
	//交易密码
	private String transactionPassword;
	//用户id
	private Integer userId;
	//设备类型
	private Integer machineType;
	
	public Integer getMachineType() {
		return machineType;
	}
	public void setMachineType(Integer machineType) {
		this.machineType = machineType;
	}
	public Integer getMarketId() {
		return marketId;
	}
	public void setMarketId(Integer marketId) {
		this.marketId = marketId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}	
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public String getTransactionPassword() {
		return transactionPassword;
	}
	public void setTransactionPassword(String transactionPassword) {
		this.transactionPassword = transactionPassword;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "OrdersVO [marketId=" + marketId + ", type=" + type + ", price=" + price + ", qty=" + qty
				+ ", transactionPassword=" + transactionPassword + ", userId=" + userId + ", machineType=" + machineType
				+ "]";
	}

}
