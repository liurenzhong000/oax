package com.oax.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.oax.common.enums.WithdrawStatusEnum;
import com.oax.entity.front.UserCoin;
import com.oax.service.UserCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.entity.front.Withdraw;
import com.oax.mapper.front.WithdrawMapper;
import com.oax.service.WithdrawService;

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

    @Autowired
    private UserCoinService userCoinService;


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
    public List<Withdraw> selectByCoinIdAndStatus(Integer coinId, byte status) {
        return withdrawMapper.selectByCoinIdAndStatus(coinId,status);
    }

    @Override
    public int updateByFall(Withdraw withdraw) {
        if (!withdraw.getStatus().equals(WithdrawStatusEnum.FALL_STATUS.getStatus())) {
            return 0;
        }
        // TODO: 2018/10/9 规则待定
        BigDecimal qty = withdraw.getQty();
        UserCoin userCoin = userCoinService.selectByUserIdAndCoinId(withdraw.getUserId(), withdraw.getCoinId());
        userCoin.setBanlance(userCoin.getBanlance().add(qty));
        userCoin.setUpdateTime(null);
        int i = withdrawMapper.updateByPrimaryKeySelective(withdraw);
        int update = userCoinService.update(userCoin);
        return update;
    }
}
