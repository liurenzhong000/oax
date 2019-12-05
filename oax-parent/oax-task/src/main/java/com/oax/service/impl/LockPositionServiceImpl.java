package com.oax.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oax.entity.front.CoinLockConfig;
import com.oax.entity.front.LockPosition;
import com.oax.entity.front.LockPositionInfo;
import com.oax.entity.front.UserCoin;
import com.oax.mapper.front.CoinLockConfigMapper;
import com.oax.mapper.front.LockPositionMapper;
import com.oax.service.LockPositionService;

@Service
public class LockPositionServiceImpl implements LockPositionService {

    @Autowired
    private LockPositionMapper mapper;

    @Autowired
    private CoinLockConfigMapper coinLockConfigMapper;

    @Transactional
    @Override
    public boolean deblocking(LockPosition lockPosition) {
        Integer count = mapper.sysDeblocking(lockPosition.getId(), lockPosition.getUserId());
        if (count!=null&&count>0) {
            UserCoin userCoin = new UserCoin();
            userCoin.setUserId(lockPosition.getUserId());
            userCoin.setCoinId(lockPosition.getCoinId());
            userCoin.setBanlance(lockPosition.getLockQty());
            Integer updateRow = mapper.addUserCoin(userCoin);
            if (updateRow!=null&&updateRow>0){
                return true;
            }
        }
        return false;
    }

    @Override
    public List<LockPositionInfo> getLockPositionList(String endTime) {
        return mapper.getLockPositionList(endTime);
    }

    @Override
    public List<CoinLockConfig> getAllCoinLockConfig() {
        return coinLockConfigMapper.getAllCoinLockConfig();
    }

    @Override
    public List<LockPosition> getExpirePositionList(String endTime) {
        return mapper.getExpirePositionList(endTime);
    }
}
