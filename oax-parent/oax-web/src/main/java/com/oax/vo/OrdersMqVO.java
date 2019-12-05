package com.oax.vo;

import java.io.Serializable;

/** 
* @ClassName:：OrdersMqVO 
* @Description： TODO
* @author ：xiangwh  
* @date ：2018年6月19日 下午8:33:56 
*  
*/
public class OrdersMqVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer marketId;
	private Integer type;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	@Override
	public String toString() {
		return "OrdersMqVO [id=" + id + ", marketId=" + marketId + ", type=" + type + "]";
	}
	
}
