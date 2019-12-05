package com.oax.service;

import java.util.List;

import com.oax.entity.front.Coin;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/3
 * Time: 18:50
 * 币种service
 */
public interface CoinService {
    List<Coin> selectAll();

    Coin selectById(Integer coinId);
}
