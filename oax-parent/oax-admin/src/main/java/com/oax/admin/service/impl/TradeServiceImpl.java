package com.oax.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.service.ITradeService;
import com.oax.entity.admin.param.SimpleCoinParam;
import com.oax.entity.admin.param.TradePageParam;
import com.oax.entity.admin.param.TradesParam;
import com.oax.entity.admin.vo.TradeFeeVo;
import com.oax.entity.admin.vo.TradePageVo;
import com.oax.entity.admin.vo.TradesVo;
import com.oax.mapper.front.TradeMapper;

@Service
public class TradeServiceImpl implements ITradeService {

    @Autowired
    private TradeMapper tradeMapper;

    @Override
    public PageInfo<TradesVo> getByUserIdQueryTrade(TradesParam tradesParam) {
        PageHelper.startPage(tradesParam.getPageNum(), tradesParam.getPageSize());
        List<TradesVo> list = tradeMapper.getByUserIdQueryTrade(tradesParam);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<TradePageVo> selectByTradePageParam(TradePageParam tradePageParam) {
        PageHelper.startPage(tradePageParam.getPageNum(), tradePageParam.getPageSize());
        List<TradePageVo> tradePageVoList = tradeMapper.selectByTradePageParam(tradePageParam);
        return new PageInfo<>(tradePageVoList);
    }

    @Override
    public PageInfo<TradeFeeVo> countTradeFee(SimpleCoinParam simpleCoin) {

        PageHelper.startPage(simpleCoin.getPageNum(),simpleCoin.getPageSize());
        List<TradeFeeVo> tradeFeeVoList = tradeMapper.countTradeFee(simpleCoin);
        return new PageInfo<>(tradeFeeVoList);
    }
}
