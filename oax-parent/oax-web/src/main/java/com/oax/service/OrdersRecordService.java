/**
 * 
 */
package com.oax.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.oax.entity.front.BargainOrdersRecord;
import com.oax.entity.front.OrdersRecord;
import com.oax.vo.OrdersQureyVO;
import com.oax.vo.TradeVO;

/** 
* @ClassName:：OrdersRecordService 
* @Description： 委托管理
* @author ：xiangwh  
* @date ：2018年6月7日 下午5:13:16 
*  
*/
public interface OrdersRecordService {
	/**
	 * 
	* @Title：findList 
	* @Description：托管订单不带分页 导出需要用到
	* @param ：@param ordersRecord
	* @param ：@return 
	* @return ：List<OrdersRecord> 
	* @throws
	 */
	List<OrdersRecord> findList(OrdersQureyVO vo);	
	/**
	* @Title：findListByPage 
	* @Description：托管订单带分页
	* @param ：@param ordersRecord
	* @param ：@param pageNum
	* @param ：@param pageSize
	* @param ：@return 
	* @return ：PageInfo<OrdersRecord> 
	* @throws
	 */
	PageInfo<OrdersRecord> findListByPage(OrdersQureyVO vo);
	/** 
	* @Title：findTradeOrderListByPage 
	* @Description：TODO
	* @param ：@param ordersRecord
	* @param ：@param pageNo
	* @param ：@param pageSize 
	* @return ：void 
	* @throws 
	*/
	PageInfo<BargainOrdersRecord> findTradeOrderListByPage(TradeVO vo);
}
