package com.oax.eth.service.impl;

import com.oax.common.BeanHepler;
import com.oax.entity.enums.UserCoinDetailType;
import com.oax.eth.service.UserCoinDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oax.entity.front.Recharge;
import com.oax.entity.front.RechargeAddress;
import com.oax.entity.front.UserCoin;
import com.oax.eth.service.RechargeService;
import com.oax.eth.service.UserCoinService;
import com.oax.mapper.front.RechargeMapper;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/20
 * Time: 21:41
 */
@Service
@Slf4j
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
    public int insertIgnore(Recharge recharge) {
        return rechargeMapper.insertIgnore(recharge);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertIgnoreAndAddUserBalance(Recharge recharge,RechargeAddress rechargeAddress) {

        int insert = this.insertIgnore(recharge);
        if (insert > 0) {
            //TODO user_coin 锁
            UserCoin userCoin = userCoinService.selectAndInsert(rechargeAddress.getUserId(), recharge.getCoinId());
            UserCoin oldUserCoin = (UserCoin) BeanHepler.clone(userCoin);
            if(recharge.getCoinId() == 54){
                userCoin.setFreezingBanlance(userCoin.getFreezingBanlance().add(recharge.getQty()));
                log.info("BHB充值直接加冻结--用户ID={}--充值数量={}-",rechargeAddress.getUserId(),recharge.getQty());
            }else {
                userCoin.setBanlance(userCoin.getBanlance().add(recharge.getQty()));
                log.info("其他eth系充值加余额--用户ID={}--充值数量={}-",rechargeAddress.getUserId(),recharge.getQty());
            }
            int succ = userCoinService.update(userCoin);
            userCoinDetailService.addUserCoinDetail(oldUserCoin, recharge.getId()+"", UserCoinDetailType.RECHARGE);
            return succ;
        }
        return insert;
    }
}
