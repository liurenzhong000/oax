package com.oax.service;

import java.math.BigDecimal;
import java.util.List;

import com.oax.entity.front.InterestShareBonus;
import com.oax.entity.front.LockPositionInfo;

public interface InterestShareBonusService {
    List<InterestShareBonus> getInterestFee(String beginTime, String endTime);

    boolean addLockInterestShareBonus(LockPositionInfo lockPositionInfo, List<InterestShareBonus> list, BigDecimal totalRatio);
}
