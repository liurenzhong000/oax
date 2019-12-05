package com.oax.service.impl;

import com.oax.entity.front.MovesayMoneyActiveList;
import com.oax.entity.front.UserCoin;
import com.oax.mapper.front.UserCoinMapper;
import com.oax.service.MovesayMoneyActiveListService;
import com.oax.service.UserCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/3
 * Time: 18:19
 */
@Service
public class UserCoinServiceImpl implements UserCoinService {

    @Autowired
    private UserCoinMapper userCoinMapper;


    @Autowired
    private MovesayMoneyActiveListService movesayMoneyActiveListService;




    public UserCoin queryBalanceInfoByUserId(Integer userId, Integer coinId){
        BigDecimal balance=new BigDecimal(0);
        UserCoin userCoin = userCoinMapper.selectByUserIdAndCoinId(userId, coinId);
        return userCoin;
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

    @Override
    public int update(UserCoin userCoin) {
        userCoin.setUpdateTime(null);
        return userCoinMapper.updateByPrimaryKeySelective(userCoin);
    }
}
