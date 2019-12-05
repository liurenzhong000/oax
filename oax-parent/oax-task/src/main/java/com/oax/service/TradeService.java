package com.oax.service;

import java.util.Date;
import java.util.List;

import com.oax.entity.admin.vo.TradesVo;
import com.oax.entity.front.Trade;
import com.oax.entity.front.vo.RechargeSumVo;
import com.oax.entity.front.vo.TradeSumVo;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/3
 * Time: 18:11
 *
 * 成交单 service
 */
public interface TradeService {
    List<Trade> selectByCoinIdAndTime(Integer coinId, Date startTime, Date endTime);

    Trade selectByleftCoinIdAndrightCoinId(int rightCoinId, int leftCoinId);


    Trade selectByMarketId(Integer marketId);

    List<TradeSumVo> selectSumVoByCoinIdAndTime(Integer coinId, Date startTime, Date endTime);
}
