package com.oax.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.admin.service.MarketService;
import com.oax.common.RedisUtil;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.entity.admin.dto.MarketWithCoin;
import com.oax.entity.admin.param.MarketParam;
import com.oax.entity.admin.vo.SimpleMarketsVo;
import com.oax.entity.front.Market;
import com.oax.mapper.front.MarketMapper;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/4
 * Time: 13:55
 */
@Service
public class MarketServiceImpl implements MarketService {

    @Autowired
    private MarketMapper marketMapper;


    @Autowired
    private RedisUtil redisUtil;


    @Override
    public List<MarketWithCoin> findAll() {
        return marketMapper.selectAll();
    }

    @Override
    public int insert(Market market) {
        int i = marketMapper.insertSelective(market);
        redisUtil.delete(RedisKeyEnum.MARKET_LIST.getKey());

        return i;
    }

    @Override
    public MarketWithCoin findById(int marketId) {
        return marketMapper.selectMarketWithCoinByPrimaryKey(marketId);
    }

    @Override
    public int update(Market market) {
        int i = marketMapper.updateByPrimaryKeySelective(market);
        redisUtil.delete(RedisKeyEnum.MARKET_LIST.getKey());
        return i;
    }


    @Override
    public List<SimpleMarketsVo> selectSimpleAll() {
        return marketMapper.selectSimpleAll();
    }

    @Override
    public List<Market> selectByCategoryCoinIdAndMarketCoinId(Integer categoryCoinId, Integer marketCoinId) {
        return marketMapper.selectByCategoryCoinIdAndMarketCoinId(categoryCoinId,marketCoinId);
    }

    @Override
    public List<MarketWithCoin> selectByMarketParam(MarketParam marketParam) {
        return marketMapper.selectByMarketParam(marketParam);
    }

    @Override
    public Market selectById(Integer id) {
        return marketMapper.selectByPrimaryKey(id);
    }
}
