/**
 * 
 */
package com.oax.service;

import java.util.List;
import java.util.Map;

import com.oax.common.ResultResponse;
import com.oax.entity.front.MarketInfo;
import com.oax.entity.front.Orders;
import com.oax.exception.VoException;
import com.oax.vo.CancelOrderVO;
import com.oax.vo.OrdersVO;

/** 
* @ClassName:：OrdersService 
* @Description：添加买卖订单
* @author ：xiangwh  
* @date ：2018年6月7日 上午9:27:17 
*  
*/
public interface OrdersService {
	/**
	 * 
	* @Title：save 
	* 添加订单
	* @param ：@param orders
	* @param ：@param member
	* @param ：@return 
	* @return ：boolean 
	* @throws
	 */
	public Integer save(OrdersVO vo,String lang,Integer version) throws VoException;
	/**
	 * 
	* @Title：getMarketOrdersList 
	* @Description：查询市场买入卖出订单(走缓存)
	* @param ：@param params
	* @param ：@return 
	* @return ：Map<String,List<MarketOrders>> 
	* @throws
	 */
	public Map<String, Object> getMarketOrdersList(int marketId);
	/**
	 * 
	* @Title：getMarketOrdersListFromDB 
	* @Description：查询市场买入卖出订单
	* @param ：@param params
	* @param ：@return 
	* @return ：Map<String,List<MarketOrders>> 
	* @throws
	 */
	public Map<String, Object> getMarketOrdersListFromDB(int marketId);
	/**
	 * 
	* @Title：getOrdersByUserId 
	* @Description：用户当前交易对的托管订单
	* @param ：@param userId
	* @param ：@param marketId
	* @param ：@return 
	* @return ：List<Map<String,String>> 
	* @throws
	 */
	public List<Map<String, String>> getOrdersByUserId(Integer userId,Integer marketId);
	/**
	 * 
	* @Title：cancelOrder 
	* @Description：撤销订单
	* @throws
	 */
	public boolean cancelOrder(CancelOrderVO vo)throws VoException;
	/**
	 * 
	* @Title：getPrecision 
	* @Description：获取交易对精度
	* @throws
	 */
	public MarketInfo  getPrecision(int marketId);
	/**
	 * 
	* @Title：isNeedTransactionPassword 
	* @Description：查询用户是否开启交易密码
	* @throws
	 */
	public String isNeedTransactionPassword(Integer userId);
	
	public ResultResponse checkUserCoin(OrdersVO vo, String lang) throws VoException ;
	public void insertUserCoin(OrdersVO vo);
	//获取订单左右币名称id
	public Orders getCoinInfo(Integer marketId);
}
