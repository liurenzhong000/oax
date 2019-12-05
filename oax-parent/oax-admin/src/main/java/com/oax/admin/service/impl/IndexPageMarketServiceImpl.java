package com.oax.admin.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.admin.service.IIndexPageMarketService;
import com.oax.admin.util.UserUtils;
import com.oax.common.RedisUtil;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.entity.admin.MarketCoin;
import com.oax.entity.admin.User;
import com.oax.entity.admin.vo.DealCoinVo;
import com.oax.entity.front.IndexPageMarket;
import com.oax.mapper.front.IndexPageMarketMapper;

@Service
public class IndexPageMarketServiceImpl implements IIndexPageMarketService {
    @Autowired
    private IndexPageMarketMapper indexPageMarketMapper;


    @Autowired
    private RedisUtil redisUtil;
    @Override
    public List<DealCoinVo> coinList() {
        List<DealCoinVo> list = indexPageMarketMapper.coinList();
        return list;
    }

    @Override
    public List<IndexPageMarket> selectByMarketId(Integer marketId) {
        return indexPageMarketMapper.selectByMarketId(marketId);
    }

    @Override
    public List<IndexPageMarket> dealRightManage() {
        List<IndexPageMarket> list = indexPageMarketMapper.dealRightManage();
        return list;
    }

    @Override
    public Integer update(MarketCoin marketCoin) {
        MarketCoin mc = new MarketCoin();
        mc.setId(marketCoin.getId());
        mc.setMarketId(marketCoin.getMarketId());
        mc.setSortNo(marketCoin.getSortNo());
        mc.setUpdateTime(new Date());
        User shiroUser = UserUtils.getShiroUser();
        mc.setAdminId(shiroUser.getId());
        Integer count = indexPageMarketMapper.update(mc);
        redisUtil.delete(RedisKeyEnum.INDEX_MARKET_LIST.getKey());
        return count;
    }


}
