package com.oax.eth.service;

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

    /**
     * 更新所有 ETH 与 TOKEN 的gasPrice
     * @param gasPrice
     * @return
     */
    int updateAllEHTAndTokenGasPrice(int gasPrice);

    int update(Coin coin);
}
