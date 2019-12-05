package com.oax.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.entity.front.UserCoin;
import com.oax.mapper.front.UserCoinMapper;
import com.oax.service.UserCoinService;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/20
 * Time: 17:14
 */
@Service
public class UserCoinServiceImpl implements UserCoinService {

    @Autowired
    private UserCoinMapper userCoinMapper;

    @Override
    public List<UserCoin> selectByAddress(String toAddress) {
        return userCoinMapper.selectByAddress(toAddress);
    }

    @Override
    public int update(UserCoin userCoin) {
        return userCoinMapper.updateByPrimaryKeySelective(userCoin);
    }

    @Override
    public UserCoin selectByUserIdAndCoinId(Integer userId, Integer coinId) {
        return userCoinMapper.selectByUserIdAndCoinId(userId, coinId);
    }


    @Override
    public List<UserCoin> selectByCoinType(int type) {
        return userCoinMapper.selectByCoinType(type);
    }

    @Override
    public int insert(UserCoin userCoin) {
        return userCoinMapper.insertSelective(userCoin);
    }

    @Override
    public UserCoin selectAndInsert(Integer userId, Integer coinId) {
        UserCoin userCoin = userCoinMapper.selectByUserIdAndCoinId(userId, coinId);
        if (userCoin != null) {
            return userCoin;
        }
        UserCoin entity = UserCoin.newInstance(userId, coinId);
        userCoinMapper.insertSelective(entity);
        entity = userCoinMapper.selectByUserIdAndCoinId(userId, coinId);
        return entity;
    }
}
