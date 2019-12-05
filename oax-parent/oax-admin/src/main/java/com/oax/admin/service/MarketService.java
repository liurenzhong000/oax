package com.oax.admin.service;

import java.util.List;

import com.oax.entity.admin.dto.MarketWithCoin;
import com.oax.entity.admin.param.MarketParam;
import com.oax.entity.admin.vo.SimpleMarketsVo;
import com.oax.entity.front.Market;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/4
 * Time: 13:53
 * 市场交易对表 service
 */
public interface MarketService {

    /**
     * 获取所有市场
     *
     * @return
     */
    List<MarketWithCoin> findAll();

    int insert(Market market);


    MarketWithCoin findById(int marketId);

    int update(Market market);

    /**
     * 获取简单 SimpleMarketsVo
     * marketsName 市场名 X/BTC
     * marketId    市场id
     *
     * @return
     */
    List<SimpleMarketsVo> selectSimpleAll();

    /**
     * 查询交易对 是否存在
     * @param categoryCoinId
     * @param marketCoinId
     * @return
     */
    List<Market> selectByCategoryCoinIdAndMarketCoinId(Integer categoryCoinId, Integer marketCoinId);

    List<MarketWithCoin> selectByMarketParam(MarketParam marketParam);

    Market selectById(Integer id);
}
