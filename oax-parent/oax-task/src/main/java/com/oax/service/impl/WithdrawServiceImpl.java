package com.oax.service.impl;

import java.util.Date;
import java.util.List;

import com.oax.entity.front.vo.WithdrawSumVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.entity.front.Withdraw;
import com.oax.mapper.front.WithdrawMapper;
import com.oax.service.WithdrawService;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/3
 * Time: 17:54
 */
@Service
public class WithdrawServiceImpl implements WithdrawService {

    @Autowired
    private WithdrawMapper withdrawMapper;

    @Override
    public List<Withdraw> selectOutByCoinIdAndTime(Integer coinId, Date startTime, Date endTime) {
        return withdrawMapper.selectOutByCoinIdAndTime(coinId, startTime, endTime);
    }

    @Override
    public List<WithdrawSumVo> selectSumVoOutByCoinIdAndTime(Integer coinId, Date startTime, Date endTime) {
        return withdrawMapper.selectSumVoOutByCoinIdAndTime(coinId, startTime, endTime);
    }
}
