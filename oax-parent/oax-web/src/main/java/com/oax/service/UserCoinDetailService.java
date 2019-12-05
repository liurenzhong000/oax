package com.oax.service;

import com.oax.entity.enums.UserCoinDetailType;
import com.oax.entity.front.UserCoin;

import java.math.BigDecimal;

/**
 * 用户资金变更表
 */
public interface UserCoinDetailService {

    void addUserCoinDetail(UserCoin oldUserCoin, String targetId, UserCoinDetailType type);
}
