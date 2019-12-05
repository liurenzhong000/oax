package com.oax.eth.service;

import com.oax.entity.enums.UserCoinDetailType;
import com.oax.entity.front.UserCoin;

public interface UserCoinDetailService {

    void addUserCoinDetail(UserCoin oldUserCoin, String targetId, UserCoinDetailType type);
}
