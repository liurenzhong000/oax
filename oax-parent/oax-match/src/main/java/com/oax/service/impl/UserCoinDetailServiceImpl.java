//package com.oax.service.impl;
//
//import com.oax.entity.enums.UserCoinDetailType;
//import com.oax.entity.front.UserCoin;
//import com.oax.entity.front.UserCoinDetail;
//import com.oax.mapper.front.UserCoinDetailMapper;
//import com.oax.mapper.front.UserCoinMapper;
//import com.oax.service.UserCoinDetailService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Date;
//
//@Service
//@Transactional
//public class UserCoinDetailServiceImpl implements UserCoinDetailService {
//
//    @Autowired
//    private UserCoinDetailMapper userCoinDetailMapper;
//    @Autowired
//    private UserCoinMapper userCoinMapper;
//
//    @Override
//    public void addUserCoinDetail(UserCoin oldUserCoin, String targetId, UserCoinDetailType type){
//        UserCoin newUserCoin = userCoinMapper.selectByUserIdAndCoinId(oldUserCoin.getUserId(), oldUserCoin.getCoinId());
//
//        UserCoinDetail userCoinDetail = new UserCoinDetail();
//        userCoinDetail.setUserId(oldUserCoin.getUserId());
//        userCoinDetail.setCoinId(oldUserCoin.getCoinId());
//        userCoinDetail.setTargetId(targetId);
//        userCoinDetail.setBeforeBalance(oldUserCoin.getBanlance());
//        userCoinDetail.setBeforeFreezing(oldUserCoin.getFreezingBanlance());
//        userCoinDetail.setAfterBalance(newUserCoin.getBanlance());
//        userCoinDetail.setAfterFreezing(newUserCoin.getFreezingBanlance());
//        userCoinDetail.setChangeBalance(userCoinDetail.getAfterBalance().subtract(userCoinDetail.getBeforeBalance()));
//        userCoinDetail.setChangeFreezing(userCoinDetail.getAfterFreezing().subtract(userCoinDetail.getBeforeFreezing()));
//        userCoinDetail.setType(type);
//        userCoinDetail.setCreateTime(new Date());
//        userCoinDetailMapper.insert(userCoinDetail);
//    }
//}