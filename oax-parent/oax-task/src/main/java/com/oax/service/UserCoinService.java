package com.oax.service;

import com.oax.entity.front.MovesayMoneyActiveList;
import com.oax.entity.front.UserCoin;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/3
 * Time: 18:19
 */
public interface UserCoinService {

    UserCoin queryBalanceInfoByUserId(Integer userId, Integer coinId);

    UserCoin selectAndInsert(Integer userId, Integer coinId);

    int update(UserCoin userCoin);

}
