package com.oax.service;

import java.util.List;

import com.oax.entity.front.CoinLockConfig;
import com.oax.entity.front.LockPosition;
import com.oax.entity.front.LockPositionInfo;

public interface LockPositionService {

    public boolean deblocking(LockPosition lockPosition);

    List<LockPositionInfo> getLockPositionList(String endTime);

    List<CoinLockConfig> getAllCoinLockConfig();

    List<LockPosition> getExpirePositionList(String endTime);
}
