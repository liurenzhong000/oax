package com.oax.outparam;

import java.io.Serializable;
import java.util.List;

import com.oax.entity.front.MarketInfo;

/** 
* @ClassName:：CategoryMarket 
* @Description： 市场分区交易对
* @author ：xiangwh  
* @date ：2018年6月15日 下午4:29:19 
*  
*/
public class CategoryMarket implements Serializable{
	private static final long serialVersionUID = -626817229445234207L;
	private String categoryName;
	private List<MarketInfo> marketList;
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public List<MarketInfo> getMarketList() {
		return marketList;
	}
	public void setMarketList(List<MarketInfo> marketList) {
		this.marketList = marketList;
	}
	@Override
	public String toString() {
		return "CategoryMarket [categoryName=" + categoryName + ", marketList=" + marketList + "]";
	}
	
	
}
