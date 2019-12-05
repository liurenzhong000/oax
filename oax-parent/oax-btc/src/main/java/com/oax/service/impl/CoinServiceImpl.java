package com.oax.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.common.enums.CoinTypeEnum;
import com.oax.entity.front.Coin;
import com.oax.mapper.front.CoinMapper;
import com.oax.service.CoinService;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/20
 * Time: 18:31
 */
@Service
public class CoinServiceImpl implements CoinService {

    @Autowired
    private CoinMapper coinMapper;

    @Override
    public List<Coin> selectByType(int type) {
        return coinMapper.selectByType(type);
    }

    @Override
    public Coin selectById(int coinId) {
        return coinMapper.selectByPrimaryKey(coinId);
    }

    @Override
    public List<Coin> selectAll() {
        return coinMapper.selectAll();
    }

    @Override
    public Coin selectByContractAddress(String contractAddress) {
        List<Coin> coinList = coinMapper.selectByContractAddress(contractAddress);
        if (CollectionUtils.isNotEmpty(coinList)) {
            return coinList.get(0);
        }
        return null;
    }
    @Override
    public List<Coin> selectUsdtCoin() {
        List<Coin> coinList = coinMapper.selectByType(CoinTypeEnum.USDT.getType());

        return coinList;
    }

    @Override
    public Coin selectBtcCoin() {
        List<Coin> coinList = coinMapper.selectByType(CoinTypeEnum.BTC.getType());
        if (CollectionUtils.isEmpty(coinList)){
            return null;
        }
        return coinList.get(0);
    }



}
