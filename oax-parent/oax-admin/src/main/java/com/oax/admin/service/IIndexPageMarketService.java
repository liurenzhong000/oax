package com.oax.admin.service;

import java.util.List;

import com.oax.entity.admin.MarketCoin;
import com.oax.entity.admin.vo.DealCoinVo;
import com.oax.entity.front.IndexPageMarket;

public interface IIndexPageMarketService {
    /*
     * 运营管理->交易对管理
     */
    List<IndexPageMarket> dealRightManage();

    /*
     * 运营管理->交易对管理->修改
     */
    Integer update(MarketCoin marketCoin);

    /*
     * 运营管理->交易对管理->修改->所有交易币种
     */
    List<DealCoinVo> coinList();

    List<IndexPageMarket> selectByMarketId(Integer marketId);
}
