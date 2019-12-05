package com.oax.service.impl;

import java.util.Date;
import java.util.List;

import com.oax.common.BeanHepler;
import com.oax.entity.enums.UserCoinDetailType;
import com.oax.entity.front.UserCoin;
import com.oax.entity.front.vo.RechargeSumVo;
import com.oax.service.UserCoinDetailService;
import com.oax.service.UserCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.entity.front.Recharge;
import com.oax.mapper.front.RechargeMapper;
import com.oax.service.RechargeService;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/3
 * Time: 17:55
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
    public List<Recharge> selectByCoinIdAndTime(Integer coinId, Date startTime, Date endTime) {
        return rechargeMapper.selectByCoinIdAndTime(coinId, startTime, endTime);
    }

    @Override
    public List<RechargeSumVo> selectSumVoByCoinIdAndTime(Integer coinId, Date startTime, Date endTime) {
        return rechargeMapper.selectSumVoByCoinIdAndTime(coinId, startTime, endTime);
    }

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
    public int insertIgnoreAndAddUserBalance(Recharge recharge) {

        int insert = this.insertIgnore(recharge);
        if (insert > 0) {
            //TODO user_coin 锁
            UserCoin userCoin = userCoinService.selectAndInsert(recharge.getUserId(), recharge.getCoinId());
            UserCoin oldUserCoin = (UserCoin) BeanHepler.clone(userCoin);
            userCoin.setBanlance(userCoin.getBanlance().add(recharge.getQty()));
            int succ = userCoinService.update(userCoin);
            userCoinDetailService.addUserCoinDetail(oldUserCoin, recharge.getId()+"", UserCoinDetailType.RECHARGE);
            return succ;
        }
        return insert;
    }
}
