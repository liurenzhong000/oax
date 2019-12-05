package com.oax.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.admin.service.WithdrawLogService;
import com.oax.entity.admin.WithdrawLog;
import com.oax.mapper.admin.WithdrawLogMapper;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/14
 * Time: 16:05
 */
@Service
public class WithdrawLogServiceImpl implements WithdrawLogService {

    @Autowired
    private WithdrawLogMapper withdrawLogMapper;


    @Override
    public int insert(WithdrawLog withdrawLog) {
        return withdrawLogMapper.insertSelective(withdrawLog);
    }

    @Override
    public List<WithdrawLog> selectByWithdrawById(int withdrawId) {
        return withdrawLogMapper.selectByWithdrawById(withdrawId);
    }
}
