package com.oax.mapper.front;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.oax.entity.admin.dto.MarketWithCoin;
import com.oax.entity.admin.param.MarketParam;
import com.oax.entity.admin.vo.SimpleMarketsVo;
import com.oax.entity.front.Market;
import com.oax.entity.front.MarketCategoryInfo;
import com.oax.entity.front.MarketCoinInfo;
import com.oax.entity.front.MarketInformation;
import com.oax.entity.front.TradeMarket;
import com.oax.entity.front.TradeRecord;

@Mapper
public interface MarketMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Market record);

    int insertSelective(Market record);

    Market selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Market record);

    int updateByPrimaryKey(Market record);

    List<MarketWithCoin> selectAll();

    /**
     * 根据id 获取市场 并带出 分区名及币种名
     *
     * @param marketId
     * @return
     */
    MarketWithCoin selectMarketWithCoinByPrimaryKey(int marketId);

    List<Market> selectListAll();

    List<Map<String, Object>> selectByCoinId(Integer coinId);

    List<TradeMarket> selectTradeByCoinId(Integer coinId);

    /**
     * 获取简单 SimpleMarketsVo
     * marketsName 市场名 X/BTC
     * marketId    市场id
     *
     * @return
     */
    List<SimpleMarketsVo> selectSimpleAll();

    /**
     * @param ：@return
     * @return ：List<Map<String,String>>
     * @throws
     * @Title：getMarketList
     * @Description：获取交易对id ，交易对名称如X/ETH
     */
    List<Map<String, Object>> getMarketList();

    List<MarketCoinInfo> selectAllMarket(@Param("market") String market);

    MarketInformation selectMarketInfo(Integer marketId);

    List<TradeRecord> getUserTrades(@Param("market") String market, @Param("userId") Integer userId);

    Integer getMarketIdByName(String market);

	/** 
	* @Title：getMarketCategoryInfo 
	* @Description：交易对
	* @throws 
	*/
	List<MarketCategoryInfo> getMarketCategoryInfo();

    List<Market> selectByCategoryCoinIdAndMarketCoinId(@Param("categoryCoinId") Integer categoryCoinId, @Param("marketCoinId") Integer marketCoinId);

    List<MarketWithCoin> selectByMarketParam(MarketParam marketParam);

	/** 
	* @Title：allMarketsOnShelf 
	* @Description：所有已上架的市场
	* @throws 
	*/
	List<Market> allMarketsOnShelf();

    List<Market> selectByType(byte status);

    List<MarketCoinInfo> selectAutoAddMarket();
}