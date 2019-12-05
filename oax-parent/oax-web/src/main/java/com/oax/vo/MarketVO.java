package com.oax.vo;

import java.io.Serializable;

/** 
* @ClassName:：MarketVO 
* @Description： 交易对查询入参
* @author ：xiangwh  
* @date ：2018年6月13日 上午10:48:00 
*  
*/
public class MarketVO implements Serializable {

	private static final long serialVersionUID = 1377098817501724706L;
	private Integer coinId;
	private Integer marketCoinId;
	public Integer getCoinId() {
		return coinId;
	}
	public void setCoinId(Integer coinId) {
		this.coinId = coinId;
	}
	public Integer getMarketCoinId() {
		return marketCoinId;
	}
	public void setMarketCoinId(Integer marketCoinId) {
		this.marketCoinId = marketCoinId;
	}
	@Override
	public String toString() {
		return "MarketVO [coinId=" + coinId + ", marketCoinId=" + marketCoinId + "]";
	}
	
	

}
