package com.oax.mapper.front;


import java.util.List;

import com.oax.entity.admin.MarketCoin;
import com.oax.entity.admin.vo.DealCoinVo;
import com.oax.entity.front.IndexPageMarket;

public interface IndexPageMarketMapper {

    IndexPageMarket selectByPrimaryKey(Integer id);

    /*
     * 运营管理->交易对管理->修改
     */
    List<IndexPageMarket> dealRightManage();


    MarketCoin queryMarketOrCoinId(Integer id);

    /**
     * 更新交易对管理排序
     */
    Integer update(MarketCoin mc);

    Integer updateSortNo(IndexPageMarket ipm);

    List<Integer> findIndexPageMarket();

    /*
     * 运营管理->交易对管理->修改->所有交易币种
     */
    List<DealCoinVo> coinList();

    List<IndexPageMarket> selectByMarketId(Integer marketId);
}