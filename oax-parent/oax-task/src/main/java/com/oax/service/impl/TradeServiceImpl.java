package com.oax.service.impl;

import java.util.Date;
import java.util.List;

import com.oax.entity.front.vo.RechargeSumVo;
import com.oax.entity.front.vo.TradeSumVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.entity.front.Trade;
import com.oax.mapper.front.TradeMapper;
import com.oax.service.TradeService;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/3
 * Time: 18:11
 */
@Service
public class TradeServiceImpl implements TradeService {

    @Autowired
    private TradeMapper tradeMapper;

    @Override
    public List<Trade> selectByCoinIdAndTime(Integer coinId, Date startTime, Date endTime) {
        return tradeMapper.selectByCoinIdAndTime(coinId, startTime, endTime);
    }

    @Override
    public Trade selectByleftCoinIdAndrightCoinId(int rightCoinId, int leftCoinId) {
        return tradeMapper.selectByleftCoinIdAndrightCoinId(rightCoinId,leftCoinId);
    }

    @Override
    public Trade selectByMarketId(Integer marketId) {
        return tradeMapper.selectByMarketId(marketId);
    }

    @Override
    public List<TradeSumVo> selectSumVoByCoinIdAndTime(Integer coinId, Date startTime, Date endTime) {
        return tradeMapper.selectSumVoByCoinIdAndTime(coinId, startTime, endTime);
    }
}
