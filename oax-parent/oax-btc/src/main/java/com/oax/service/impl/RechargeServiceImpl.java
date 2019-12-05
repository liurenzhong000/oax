package com.oax.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.oax.common.BeanHepler;
import com.oax.entity.enums.UserCoinDetailType;
import com.oax.service.UserCoinDetailService;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oax.entity.front.Recharge;
import com.oax.entity.front.UserCoin;
import com.oax.mapper.front.RechargeMapper;
import com.oax.service.RechargeService;
import com.oax.service.UserCoinService;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/20
 * Time: 21:41
 */
@Service
public class RechargeServiceImpl implements RechargeService {

    @Autowired
    private RechargeMapper rechargeMapper;

    @Autowired
    private UserCoinService userCoinService;

    @Autowired
    private UserCoinDetailService userCoinDetailService;

    @Override
    public int insert(Recharge recharge) {
        return rechargeMapper.insertSelective(recharge);
    }

    @Override
    public List<Recharge> selectByToAddress(String address) {
        return rechargeMapper.selectByToAddress(address);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertAndaddBalance(Recharge recharge, UserCoin userCoin) {
        int insert = rechargeMapper.insertSelective(recharge);
        UserCoin oldUserCoin = userCoinService.selectByUserIdAndCoinId(userCoin.getUserId(), userCoin.getCoinId());
        if (insert>0){
            BigDecimal banlance = userCoin.getBanlance();
            userCoin.setBanlance(banlance.add(recharge.getQty()));
            userCoin.setUpdateTime(null);
            userCoinService.update(userCoin);
            //TODO user_coin 锁
            userCoinDetailService.addUserCoinDetail(oldUserCoin, recharge.getId()+"", UserCoinDetailType.RECHARGE);
        }
        return insert;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertIgnoreAndAddUserBalance(Recharge recharge) {
        int i = rechargeMapper.insertIgnore(recharge);
        if (i>0){
            UserCoin userCoin = userCoinService.selectAndInsert(recharge.getUserId(), recharge.getCoinId());
            //TODO user_coin 锁
            UserCoin oldUserCoin = (UserCoin) BeanHepler.clone(userCoin);
            userCoin.setBanlance(userCoin.getBanlance().add(recharge.getQty()));
            int succ = userCoinService.update(userCoin);
            userCoinDetailService.addUserCoinDetail(oldUserCoin, recharge.getId()+"", UserCoinDetailType.RECHARGE);
            return succ;
        }
        return i;
    }

    @Override
    public List<Recharge> selectAll() {
        return rechargeMapper.selectAll();
    }
}
