package com.oax.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oax.common.enums.CoinEnum;
import com.oax.entity.front.InvitingFeedback;
import com.oax.entity.front.UserCoin;
import com.oax.mapper.front.InvitingFeedbackMapper;
import com.oax.mapper.front.TradeFeedBackLogMapper;
import com.oax.service.InvitingFeedbackService;
@Service
public class InvitingFeedbackServiceImpl implements InvitingFeedbackService {
    @Autowired
    private InvitingFeedbackMapper mapper;
    @Autowired
    private TradeFeedBackLogMapper tradeFeedBackLogMapper;
    @Override
    public List<InvitingFeedback> pullInvitingFeedbackList(String beginTime, String endTime) {
        return mapper.pullInvitingFeedbackList(beginTime, endTime);
    }


    @Transactional
    @Override
    public boolean save(InvitingFeedback invitingFeedback) {
        Integer saveCount = mapper.save(invitingFeedback);
        if (saveCount!=null&&saveCount>0){
            //修改用户X币资产数据
            UserCoin userCoin = new UserCoin();
            userCoin.setUserId(invitingFeedback.getUserId());
            //X币id=3
            userCoin.setCoinId(CoinEnum.TOKEN_X.getValue());
            userCoin.setBanlance(invitingFeedback.getQty());
            Integer count = tradeFeedBackLogMapper.selectUserCoin(userCoin);
            if (count!=null&&count>0){
                //如果该用户有X币资产  修改资产数据即可
                tradeFeedBackLogMapper.updateUserCoinByUserIdAndCoinId(userCoin);
            }else{
                //如果该用户没有X币资产 则添加用户X币资产数据
                userCoin.setFreezingBanlance(BigDecimal.ZERO);
                userCoin.setCreateTime(new Date());
                userCoin.setUpdateTime(new Date());
                //插入x币资产
                tradeFeedBackLogMapper.insertUserCoin(userCoin);
            }
            return true;
        }
        return false;
    }
}
