package com.oax.eth.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.entity.front.Withdraw;
import com.oax.eth.service.WithdrawService;
import com.oax.mapper.front.WithdrawMapper;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/25
 * Time: 19:04
 */
@Service
public class WithdrawServiceImpl implements WithdrawService {
    @Autowired
    private WithdrawMapper withdrawMapper;


    @Override
    public List<Withdraw> selectByStatus(byte inTxpoolStatus) {
        return withdrawMapper.selectByStatus(inTxpoolStatus);
    }

    @Override
    public int update(Withdraw withdraw) {
        withdraw.setUpdateTime(null);
        return withdrawMapper.updateByPrimaryKeySelective(withdraw);
    }

    @Override
    public Withdraw selectByHash(String hash) {
        return withdrawMapper.selectByHash(hash);

    }

    @Override
    public Withdraw selectById(int withdrawId) {
        return withdrawMapper.selectByPrimaryKey(withdrawId);
    }
}
