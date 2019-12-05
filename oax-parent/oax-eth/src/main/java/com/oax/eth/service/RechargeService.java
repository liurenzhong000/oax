package com.oax.eth.service;

import com.oax.entity.front.Recharge;
import com.oax.entity.front.RechargeAddress;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/20
 * Time: 21:41
 * 转入记录 service
 */
public interface RechargeService {
    int insert(Recharge recharge);

    int insertIgnore(Recharge recharge);

    int insertIgnoreAndAddUserBalance(Recharge recharge,RechargeAddress rechargeAddress);
}
