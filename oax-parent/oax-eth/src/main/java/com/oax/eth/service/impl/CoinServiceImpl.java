package com.oax.eth.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.entity.front.Coin;
import com.oax.entity.front.CoinWithBLOBs;
import com.oax.eth.service.CoinService;
import com.oax.mapper.front.CoinMapper;

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
        return coinMapper.selectById(coinId);
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
    public int updateAllEHTAndTokenGasPrice(int gasPrice) {
        return coinMapper.updateAllEHTAndTokenGasPrice(gasPrice);
    }

    @Override
    public int update(Coin coin) {
        CoinWithBLOBs coinWithBLOBs = new CoinWithBLOBs();
        BeanUtils.copyProperties(coin,coinWithBLOBs);
        //这些字段不更新
        coinWithBLOBs.setUpdateTime(null);
        coin.setMainAddress(null);
        coin.setMainAddressPassword(null);
        coin.setColdAddress(null);
        coin.setServerIp(null);
        coin.setServerPort(null);
        return coinMapper.updateByPrimaryKeySelective(coinWithBLOBs);
    }


}
