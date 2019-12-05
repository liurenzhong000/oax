/**
 * 
 */
package com.oax.service;

import java.util.List;
import java.util.Map;

import com.oax.entity.front.MarketInfo;
import com.oax.outparam.CategoryMarket;

/** 
* @ClassName:：TradeCoinService 
* @Description： 交易对信息
* @author ：xiangwh  
* @date ：2018年6月5日 上午10:41:55 
*  
*/

public interface TradeCoinService {
	/**
	 * 
	* @Title：findList 
	* @Description：获取所有的交易对的交易信息
	* @param ：@return 
	* @return ：List<TradeCoin> 
	* @throws
	 */
	List<CategoryMarket>findList(Integer userId);
	/**
	* @Title：getHomeList 
	* @Description：首页获取前 #{sortNum}-1个交易对的交易信息跟k线图数据(24小时)
	* @param ：@param sortNum
	* @param ：@return 
	* @return ：List<Map<String,Object>> 
	* @throws
	 */
	List<Map<String, Object>> findIndexPageMarket();
	/**
	 * 
	* @Title：findListFromDB 
	* @Description：与findList接口一样，直接从db中获取数据，不需要走redis，保证websocket推送是数据具有实时性
	* @param ：@param tradeCoin
	* @param ：@return 
	* @return ：List<TradeCoin> 
	* @throws
	 */
	List<MarketInfo> findListFromDB();
}
