/**
 * 
 */
package com.oax.service;

import java.util.List;
import java.util.Map;

import com.oax.entity.front.Market;
import com.oax.entity.front.MarketCategoryResult;

/** 
* @ClassName:：MarketService 
* @Description：交易对service接口
* @author ：xiangwh  
* @date ：2018年6月6日 下午10:44:08 
*  
*/
public interface MarketService {
	List<Map<String, Object>> getMarketList();
	List<Market> selectAll();
	List<MarketCategoryResult>getMarketCategoryInfo();
}
