package com.oax.vo;

import java.io.Serializable;

/** 
* @ClassName:：KlineVO 
* @Description： k线图入参
* @author ：xiangwh  
* @date ：2018年6月13日 下午3:33:49 
*  
*/
public class KlineVO implements Serializable {

	private static final long serialVersionUID = 49012361646145579L;
	
	private Integer marketId;
	private Integer minType;
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
		return "KlineVO [marketId=" + marketId + ", minType=" + minType + "]";
	}
	
	

}
