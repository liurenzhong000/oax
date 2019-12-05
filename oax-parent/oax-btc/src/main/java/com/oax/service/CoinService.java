package com.oax.service;

import java.util.List;

import com.oax.entity.front.Coin;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/20
 * Time: 18:31
 * 币种 service
 */
public interface CoinService {
    List<Coin> selectByType(int type);

    Coin selectById(int coinId);

    List<Coin> selectAll();

    Coin selectByContractAddress(String contractAddress);

    List<Coin> selectUsdtCoin();

    Coin selectBtcCoin();
}
