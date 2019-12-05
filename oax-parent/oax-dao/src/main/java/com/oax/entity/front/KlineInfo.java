package com.oax.entity.front;

import java.io.Serializable;

/** 
* @ClassName:：KlineInfo 
* @Description： 
* @author ：xiangwh  
* @date ：2018年7月5日 上午10:15:56 
*  
*/
public class KlineInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer marketId;
	private Integer minType;
	private Integer amount;
	public Integer getMarketId() {
		return marketId;
	}
	public void setMarketId(Integer marketId) {
		this.marketId = marketId;
	}
	public Integer getMinType() {
		return minType;
	}
	public void setMinType(Integer minType) {
		this.minType = minType;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "KlineInfo [marketId=" + marketId + ", minType=" + minType + ", amount=" + amount + "]";
	}
	

}
