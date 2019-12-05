package com.oax.admin.service;

import com.github.pagehelper.PageInfo;
import com.oax.entity.admin.param.ActivityCoinParam;
import com.oax.entity.admin.vo.UserCoinDetailVo;
import com.oax.entity.enums.UserCoinDetailType;
import com.oax.entity.front.UserCoin;

public interface UserCoinDetailService {

    void addUserCoinDetail(UserCoin oldUserCoin, String targetId, UserCoinDetailType type);

    PageInfo<UserCoinDetailVo> getActivityUserDetailList(ActivityCoinParam param);
}
