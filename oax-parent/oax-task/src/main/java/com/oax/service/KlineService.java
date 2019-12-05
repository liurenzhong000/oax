package com.oax.service;

import java.util.List;
import java.util.Map;

import com.oax.entity.front.Kline;
import com.oax.entity.front.KlineInfo;

/** 
* @ClassName:：KlineService 
* @Description： K线接口
* @author ：xiangwh  
* @date ：2018年7月3日 下午3:18:28 
*  
*/
public interface KlineService {
	public List<Kline> findListByMarketId(Map<String, Object> params);
	public List<Kline> findListByMarketIdGtOrLt(Integer marketId);
	/** 
	* @Title：pullKline 
	* @Description：拉取有交易记录的市场的k线数据kline数据
	* @throws 
	*/
	public List<Kline> pullKline(String beginDate, String endDate,Integer minType);
	/**
	* @Title：batchSave 
	* @Description：	 批量插入k线图数据
	* @throws
	 */
	public boolean batchSave(List<Kline> list);
	/**
	 * @param minType 
	 * @param marketId  
	* @Title：removeKline 
	* @Description：将超过1500条的市场的k线数据插入到历史表中
	* @throws 
	*/
	public void removeKline(List<KlineInfo>list);
	/** 
	* @Title：selectList 
	* @Description：
	* @throws 
	*/
	public List<KlineInfo> selectList();
	/**
	 * 
	* @Title：findListByMarketIdFromDB 
	* @Description：根据marketId和minType查询k线
	* @throws
	 */
	void setKlineRedis(List<Kline>list);
}
