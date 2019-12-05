package com.oax.mapper.front;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.oax.entity.admin.vo.FeedBackVo;
import com.oax.entity.front.Trade;
import com.oax.entity.front.TradeFeedBack;
import com.oax.entity.front.TradeSnapshot;
import com.oax.entity.front.UserCoin;

@Mapper
public interface TradeFeedBackLogMapper {
    List<TradeSnapshot> getTradeSnapshotByYesterday(@Param("minDate") String minDate, @Param("maxDate")String maxDate);

    List<Trade> getWaitFeedBackTradeList(TradeSnapshot tradeSnapshot);

    Integer updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    Integer insert(TradeFeedBack tradeFeedBackLog);

    Integer updateTrade(Integer id);

    Integer selectUserCoin(UserCoin userCoin);

    void updateUserCoinByUserIdAndCoinId(UserCoin userCoin);

    void insertUserCoin(UserCoin userCoin);

    BigDecimal getMyFeedBack(@Param("userId") Integer userId,@Param("beginTime") String beginTime,@Param("endTime") String endTime);

    List<Map<String,Object>> selectAll(FeedBackVo vo);

    Map<String,Object> collectFeedBack(FeedBackVo vo);
}
