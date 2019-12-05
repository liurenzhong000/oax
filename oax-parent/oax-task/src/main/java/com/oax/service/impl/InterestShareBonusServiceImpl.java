package com.oax.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.entity.front.InterestShareBonus;
import com.oax.entity.front.LockPositionInfo;
import com.oax.entity.front.UserCoin;
import com.oax.mapper.front.InterestShareBonusMapper;
import com.oax.mapper.front.TradeFeedBackLogMapper;
import com.oax.service.InterestShareBonusService;

@Service
public class InterestShareBonusServiceImpl implements InterestShareBonusService{
    @Autowired
    private InterestShareBonusMapper mapper;
    @Autowired
    private TradeFeedBackLogMapper tradeFeedBackLogMapper;


    @Override
    public List<InterestShareBonus> getInterestFee(String beginTime, String endTime) {
        return mapper.getInterestFee(beginTime, endTime);
    }

    @Override
    public boolean addLockInterestShareBonus(LockPositionInfo info, List<InterestShareBonus> list, BigDecimal totalRatio) {
        InterestShareBonus shareBonus = getInterestShareBonus(info.getCoinId(), list);
        if (shareBonus!=null){
            InterestShareBonus model = new InterestShareBonus();
            //该锁仓单的权重系数
            BigDecimal singleRatio = info.getRatio().multiply(info.getLockQty()).divide(info.getStandardLockQty(), 8, BigDecimal.ROUND_FLOOR);
            //锁仓利息
            BigDecimal qty = shareBonus.getQty().multiply(info.getInterestRate()).multiply(singleRatio).divide(totalRatio, 8,BigDecimal.ROUND_FLOOR);
            model.setCoinId(info.getCoinId());
            model.setLockPositionId(info.getId());
            model.setUserId(info.getUserId());
            model.setCreateTime(new Date());
            model.setQty(qty);
            //对锁仓进行分红
            Integer insertRow = mapper.insert(model);
            if (insertRow!=null&&insertRow>0){
                //修改资产数据  只需要修改，因为该用户能锁仓，说明肯定有该资产
                UserCoin userCoin = new UserCoin();
                userCoin.setUserId(model.getUserId());
                //X币id=3
                userCoin.setCoinId(model.getCoinId());
                userCoin.setBanlance(model.getQty());
                    //如果该用户有X币资产  修改资产数据即可
                tradeFeedBackLogMapper.updateUserCoinByUserIdAndCoinId(userCoin);
                return true;
            }
            return false;
        }else{
            return true;
        }

    }

    private InterestShareBonus getInterestShareBonus(Integer coinId,List<InterestShareBonus> list){
        InterestShareBonus interestShareBonus = null;
        for (InterestShareBonus model : list) {
            if (coinId==model.getCoinId()){
                interestShareBonus = model;
                break;
            }
        }
        return interestShareBonus;
    }
}
