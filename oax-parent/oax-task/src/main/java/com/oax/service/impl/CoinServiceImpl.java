package com.oax.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.entity.front.Coin;
import com.oax.mapper.front.CoinMapper;
import com.oax.service.CoinService;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/3
 * Time: 18:51
 */
@Service
public class CoinServiceImpl implements CoinService {
    @Autowired
    private CoinMapper coinMapper;

    @Override
    public List<Coin> selectAll() {
        return coinMapper.selectAll();
    }

    @Override
    public Coin selectById(Integer coinId) {
        return coinMapper.selectById(coinId);
    }
}
