package com.oax.eth.service;

import java.util.List;

import com.oax.entity.front.UserCoin;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/20
 * Time: 17:14
 * 用户地址
 */
public interface UserCoinService {

    List<UserCoin> selectAllETHAdress();

    List<UserCoin> selectByAddress(String toAddress);

    int update(UserCoin userCoin);

    UserCoin selectByUserIdAndCoinId(Integer userId, Integer coinId);

    int insert(UserCoin userCoin);

    int insertIgnore(UserCoin userCoin);

    UserCoin selectAndInsert(Integer userId, Integer coinId);
}
