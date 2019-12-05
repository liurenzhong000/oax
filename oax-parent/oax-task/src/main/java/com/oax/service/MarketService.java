/**
 * 
 */
package com.oax.service;

import java.util.List;
import java.util.Map;

import com.oax.entity.front.Market;
import com.oax.entity.front.MarketInfo;
import com.oax.entity.front.TradeInfo;

/** 
* @ClassName:：MarketService 
* @Description：交易对service接口
* @author ：xiangwh  
* @date ：2018年6月6日 下午10:44:08 
*  
*/
public interface MarketService {
	List<Market> selectAll();
	//查询已上架的所有的市场的基本信息
	List<Market> allMarketsOnShelf();
	//所有已上架交易对的综合信息
	List<MarketInfo> allMarketInfo();
	//单个市场的买卖委托深度
	Map<String, Object> getMarketOrdersMap(Integer marketId);
	//单个市场的实时交易记录
	List<TradeInfo> getMarketTradeList(Integer marketId);

    List<Market> selectByType(byte status);
}
