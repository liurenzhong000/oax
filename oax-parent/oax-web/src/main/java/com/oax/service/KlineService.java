/**
 * 
 */
package com.oax.service;

import java.util.List;

import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.front.Kline;
import com.oax.vo.KlineVO;

/** 
* @ClassName:：KlineService 
* @Description： 获取k线图数据的service接口
* @author ：xiangwh  
* @date ：2018年6月6日 下午2:31:02 
*  
*/
public interface KlineService {
	/**
	 * 
	* @Title：findListByMarketId 
	* @Description：根据时间类型跟交易对id查询k线图数据
	* @param ：@param kline
	* @param ：@return 
	* @return ：List<Kline> 
	* @throws
	 */
	List<Kline> findListByMarketId(KlineVO vo);

    List<List<Object>> findListByMarketIdNew(KlineVO vo);

    /**
	 * 
	* @Title：findListByMarketIdGtOrLt 
	* @Description：根据flag 判断 如果flag==lt说明查询的是小于一天的数据  用户websocket消息推送
	* @param ：@param flag
	* @param ：@return 
	* @return ：List<Kline> 
	* @throws
	 */
	List<Kline> findListByMarketIdGtOrLt(int marketId);
	/**
	 * 
	* @Title：findListByMarketIdFromDB 
	* @Description：与findListByMarketId接口一样，直接从db中获取
	* @param ：@param kline
	* @param ：@return 
	* @return ：List<Kline> 
	* @throws
	 */
	List<Kline> findListByMarketIdFromDB(KlineVO vo);
}
