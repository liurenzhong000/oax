/**
 * 
 */
package com.oax.service;

import java.util.List;

import com.oax.entity.front.TradeInfo;

/** 
* @ClassName:：TradeService 
* @Description： 交易记录接口
* @author ：xiangwh  
* @date ：2018年6月5日 下午2:11:50 
*  
*/
public interface TradeService {
	/**
	 * 
	* @Title：getTradeListByMarketId 
	* @Description：查询用户或者市场的对应的交易对的实时交易记录
	* @param ：@param trade
	* @param ：@return 
	* @return ：List<Trade> 
	* @throws
	 */
	List<TradeInfo> getTradeList(Integer marketId,Integer userId);
	
	/**
	 * 
	* @Title：getTradeListByMarketIdFromDB 
	* @Description：TODO
	* @param ：@param trade
	* @param ：@return 
	* @return ：List<Trade> 
	* @throws
	 */
	List<TradeInfo> getTradeListByMarketIdFromDB(int marketId);
}
