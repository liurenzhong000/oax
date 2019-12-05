package com.oax.service;

import java.util.List;

import com.oax.entity.front.Recharge;
import com.oax.entity.front.UserCoin;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/20
 * Time: 21:41
 * 转入记录 service
 */
public interface RechargeService {
    int insert(Recharge recharge);

    List<Recharge> selectByToAddress(String address);

    int insertAndaddBalance(Recharge recharge, UserCoin userCoin);

    int insertIgnoreAndAddUserBalance(Recharge recharge);

    List<Recharge> selectAll();

}
