package com.oax.entity.front;

import java.util.List;

/** 
* @ClassName:：MarketCategoryResult 
* @Description： TODO
* @author ：xiangwh  
* @date ：2018年6月29日 下午7:02:50 
*  
*/
public class MarketCategoryResult {
	private String categoryName;
	private List<MarketCategoryInfo> MarketCategoryList;
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public List<MarketCategoryInfo> getMarketCategoryList() {
		return MarketCategoryList;
	}
	public void setMarketCategoryList(List<MarketCategoryInfo> marketCategoryList) {
		MarketCategoryList = marketCategoryList;
	}
	@Override
	public String toString() {
		return "MarketCategoryResult [categoryName=" + categoryName + ", MarketCategoryList=" + MarketCategoryList
				+ "]";
	}	 

}
