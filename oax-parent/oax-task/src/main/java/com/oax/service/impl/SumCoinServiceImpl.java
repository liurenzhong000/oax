package com.oax.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.entity.front.SumCoin;
import com.oax.mapper.front.SumCoinMapper;
import com.oax.service.SumCoinService;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/3
 * Time: 18:34
 */
@Service
public class SumCoinServiceImpl implements SumCoinService {
    @Autowired
    private SumCoinMapper sumCoinMapper;

    @Override
    public int insert(SumCoin sumCoin) {
        return sumCoinMapper.insertSelective(sumCoin);
    }
}
