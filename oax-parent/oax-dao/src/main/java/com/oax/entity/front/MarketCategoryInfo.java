package com.oax.entity.front;
/** 
* @ClassName:：MarketCategoryInfo 
* @Description： 交易对信息
* @author ：xiangwh  
* @date ：2018年6月29日 下午5:58:52 
*  
*/
public class MarketCategoryInfo {
	private Integer marketId ; 
	private String  marketMame;
	
	private String categoryName;

	public Integer getMarketId() {
		return marketId;
	}

	public void setMarketId(Integer marketId) {
		this.marketId = marketId;
	}

	public String getMarketMame() {
		return marketMame;
	}

	public void setMarketMame(String marketMame) {
		this.marketMame = marketMame;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Override
	public String toString() {
		return "MarketCategoryInfo [marketId=" + marketId + ", marketMame=" + marketMame + ", categoryName="
				+ categoryName + "]";
	}

	
	
	
	
}
