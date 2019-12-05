package com.oax.vo;

import java.io.Serializable;

/** 
* @ClassName:：TransactionPageVO 
* @Description： 交易页面参数
* @author ：xiangwh  
* @date ：2018年6月23日 下午1:21:20 
*  
*/
public class TransactionPageVO implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer userId;
	private Integer marketId;
	private Integer minType;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
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
	@Override
	public String toString() {
		return "TransactionPageVO [userId=" + userId + ", marketId=" + marketId + ", minType=" + minType + "]";
	}
	
	

}
