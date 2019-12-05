package com.oax.mapper.front;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.oax.entity.front.vo.RechargeSumVo;
import com.oax.entity.front.vo.TradeSumVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.oax.entity.admin.param.SimpleCoinParam;
import com.oax.entity.admin.param.TradePageParam;
import com.oax.entity.admin.param.TradesParam;
import com.oax.entity.admin.vo.TradeFeeVo;
import com.oax.entity.admin.vo.TradePageVo;
import com.oax.entity.admin.vo.TradesVo;
import com.oax.entity.front.MarketPriceInfo;
import com.oax.entity.front.Trade;
import com.oax.entity.front.TradeInfo;

@Mapper
public interface TradeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Trade record);

    int insertSelective(Trade record);

    Trade selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Trade record);

    int updateByPrimaryKey(Trade record);

    /**
     * @param ：@param trade
     * @return ：List<Trade>
     * @throws
     * @Title：getTradeListByMarketId
     * @Description：查询单个交易对的市场实时交易记录(可以加入userid过滤查询)
     * @author:xiangwh
     */
    List<TradeInfo> getTradeList(@Param("marketId") Integer marketId, @Param("userId") Integer userId);

    List<TradesVo> getByUserIdQueryTrade(TradesParam tradesParam);

    /**
     * 根据 交易记录参数 查询交易记录
     *
     * @param tradePageParam userId      用户id
     *                       marketId    市场id
     *                       orderType   交易记录状态
     *                       pageNo      页码
     *                       pageSize    一页展示数
     *                       startTime   开始
     *                       endTime     结束
     * @return
     */
    List<TradePageVo> selectByTradePageParam(TradePageParam tradePageParam);

	MarketPriceInfo selectLastPriceByCoinIdAndEthId(@Param("ethId")Integer ethId,@Param("coinId") Integer coinId);

    List<Trade> selectByCoinIdAndTime(@Param("coinId") Integer coinId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<TradeFeeVo> countTradeFee(SimpleCoinParam simpleCoin);

    Trade selectByleftCoinIdAndrightCoinId(@Param("rightCoinId") int rightCoinId,
                                           @Param("leftCoinId") int leftCoinId);

    List<Trade> selectByleftCoinIdAndrightCoinIdAndTime(@Param("leftCoinId") int leftCoinId,
                                                        @Param("rightCoinId") int rightCoinId,
                                                        @Param("startTime") Date startTime,
                                                        @Param("endTime") Date endTime);

    Trade selectByMarketId(Integer marketId);

    BigDecimal getYesterdayAvgByMarketId(Integer marketId);

    BigDecimal getAvgByMarketIdAndTime(@Param("marketId")Integer marketId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    BigDecimal getRatioByLeftIdAndRightId(@Param("leftCoinId") Integer leftCoinId, @Param("rightCoinId") Integer rightCoinId);

    List<TradeSumVo> selectSumVoByCoinIdAndTime(@Param("coinId") Integer coinId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}