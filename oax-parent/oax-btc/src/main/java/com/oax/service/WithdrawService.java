package com.oax.service;

import java.util.List;

import com.oax.entity.front.Withdraw;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/25
 * Time: 19:03
 * 用户转出记录(主地址) 状态维护
 */
public interface WithdrawService {


    List<Withdraw> selectByStatus(byte inTxpoolStatus);

    int update(Withdraw withdraw);

    Withdraw selectByHash(String hash);

    List<Withdraw> selectByCoinIdAndStatus(Integer coinId, byte status);

    int updateByFall(Withdraw withdraw);
}
