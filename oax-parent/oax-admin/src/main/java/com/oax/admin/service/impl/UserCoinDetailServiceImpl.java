package com.oax.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.service.UserCoinDetailService;
import com.oax.entity.admin.param.ActivityCoinParam;
import com.oax.entity.admin.vo.UserCoinDetailVo;
import com.oax.entity.enums.UserCoinDetailType;
import com.oax.entity.front.UserCoin;
import com.oax.entity.front.UserCoinDetail;
import com.oax.mapper.front.UserCoinDetailMapper;
import com.oax.mapper.front.UserCoinMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserCoinDetailServiceImpl implements UserCoinDetailService {

    @Autowired
    private UserCoinDetailMapper userCoinDetailMapper;

    @Autowired
    private UserCoinMapper userCoinMapper;

    @Override
    public void addUserCoinDetail(UserCoin oldUserCoin, String targetId, UserCoinDetailType type){
        UserCoin newUserCoin = userCoinMapper.selectByUserIdAndCoinId(oldUserCoin.getUserId(), oldUserCoin.getCoinId());

        UserCoinDetail userCoinDetail = new UserCoinDetail();
        userCoinDetail.setUserId(oldUserCoin.getUserId());
        userCoinDetail.setCoinId(oldUserCoin.getCoinId());
        userCoinDetail.setTargetId(targetId);
        userCoinDetail.setBeforeBalance(oldUserCoin.getBanlance());
        userCoinDetail.setBeforeFreezing(oldUserCoin.getFreezingBanlance());
        userCoinDetail.setAfterBalance(newUserCoin.getBanlance());
        userCoinDetail.setAfterFreezing(newUserCoin.getFreezingBanlance());
        userCoinDetail.setChangeBalance(userCoinDetail.getAfterBalance().subtract(userCoinDetail.getBeforeBalance()));
        userCoinDetail.setChangeFreezing(userCoinDetail.getAfterFreezing().subtract(userCoinDetail.getBeforeFreezing()));
        userCoinDetail.setType(type);
        userCoinDetail.setCreateTime(new Date());
        userCoinDetailMapper.insert(userCoinDetail);
    }

    @Override
    public PageInfo<UserCoinDetailVo> getActivityUserDetailList(ActivityCoinParam param) {
        PageHelper.startPage(param.getPageNum(),param.getPageSize());
        List<UserCoinDetailVo> tradeFeeVoList = userCoinDetailMapper.getActivityUserDetailList(param);
        tradeFeeVoList.forEach(item-> item.setTypeDeac(item.getType().desc));
        return new PageInfo<>(tradeFeeVoList);
    }

}
