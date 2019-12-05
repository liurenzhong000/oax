package com.oax.mapper.front;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.oax.entity.front.Kline;
import com.oax.entity.front.KlineInfo;

@Mapper
public interface KlineMapper {
    int insert(Kline record);

    int insertSelective(Kline record);

    /**
     * @param ：@param  params
     * @param ：@return
     * @return ：List<Kline>
     * @throws
     * @Title：findListByMarketId
     * @Description：根据marketId 和minType 查询k线图数据
     */
    List<Kline> findListByMarketId(Map<String, Object> params);

    List<Kline> findListByMarketIdGtOneDay(int marketId);

	/** 
	* @Title：pullKline 
	* @Description：拉取单条k线数据
	* @throws 
	*/
	List<Kline> pullKline(@Param("beginDate")String beginDate, @Param("endDate")String endDate,@Param("minType")Integer minType);

	/** 
	* @Title：batchSave 
	* @Description：批量插入市场的k线数据
	* @throws 
	*/
	Integer batchSave(List<Kline> list);

	/**
	 * @param minType 
	 * @param marketId  
	* @Title：removeKline 
	* @Description：将超过1500条的市场的k线数据插入到历史表中
	* @throws 
	*/
	void removeKline(@Param("marketId")Integer marketId, @Param("minType")Integer minType,@Param("ids")List<Integer>ids);

	/** 
	* @Title：selectListGT1500 
	* @Description：k线分组>1500条的分组数据
	* @throws 
	*/
	List<KlineInfo> selectListGT1500();

	/** 
	* @Title：getKlineIds 
	* @Description：获取最新的市场的1500条k线数据
	* @throws 
	*/
	List<Integer> getKlineIds(@Param("marketId")Integer marketId, @Param("minType")Integer minType);

	/**
	 * @param integer2 
	 * @param integer  
	* @Title：deleteKline 
	* @Description：删除旧的k线数据
	* @throws 
	*/
	void deleteKline(@Param("marketId")Integer marketId, @Param("minType")Integer minType, @Param("ids")List<Integer> ids);

	/** 
	* @Title：getKlineList 
	* @Description：
	* @throws 
	*/
	List<Kline> getKlineList(@Param("marketId")Integer marketId, @Param("minType")Integer minType);

	List<Kline> getKlineListLimit(@Param("marketId")Integer marketId, @Param("minType")Integer minType);

	/** 
	* @Title：batchSaveGt1440 
	* @Description：
	* @throws 
	*/
	Integer batchSaveGt1440(List<Kline> list);
	/**
	 * @Description：获取前一天markId的24小时k线
	 *
	 */
	Kline findYesterdayKline(Integer marketId);
	/**
	 * @Description：获取最后number条markId的minTypek线
	 *
	 */
	List<Kline> getKlineListLast(@Param("marketId")int markerId,  @Param("minType")int minType,@Param("number")int number);
}