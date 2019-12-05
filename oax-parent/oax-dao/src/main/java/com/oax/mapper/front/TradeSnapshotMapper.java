package com.oax.mapper.front;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.oax.entity.front.MarketInfo;
import com.oax.entity.front.Trade;
import com.oax.entity.front.TradeSnapshot;

@Mapper
public interface TradeSnapshotMapper {
    List<Integer> getMarketIds();

    List<Trade> getTradeList(@Param("beginTime") String beginTime, @Param("endTime")String endTime, @Param("marketId") Integer marketId);
    //获取最新价格
    BigDecimal getLastPrice(Integer marketId);

    MarketInfo getMarketIdByCoinId(@Param("leftCoinId") Integer leftCoinId, @Param("rightCoinId")Integer rightCoinId);
    
    Integer insert(TradeSnapshot tradeSnapshot);

    BigDecimal getTradeListForAvg(@Param("beginTime")String beginTime, @Param("endTime")String endTime, @Param("marketId")Integer marketId);

    BigDecimal getCirculationTotal();

    Map<String,BigDecimal> getFeedBackSum(String beginTime);

    List<Map<String, Object>> getTodayFeedBackdetail(String beginTime);

    BigDecimal getBanlanceX(Integer userId);

    BigDecimal getFeedBackToDay(String beginTime);

    List<Map<String,Object>> MiningMarket();
}
