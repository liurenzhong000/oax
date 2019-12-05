package com.oax.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.oax.entity.front.CoinLockConfig;
import com.oax.vo.LockPositionRecordVO;
import com.oax.vo.LockPositionVO;

public interface LockPositionService {
    CoinLockConfig selectCoinLockConfigById(Integer id);

    BigDecimal getUserCoinByCoinId(Integer userId, Integer coinId);

    boolean lock(LockPositionVO vo);

    CoinLockConfig selectCoinLockConfigByCoinId(Integer coinId);

    boolean declock(Integer id, Integer userId);

    PageInfo getPage(LockPositionRecordVO vo);

    List<Map<String,Object>> getInterestSharebonusList(Integer lockPositionId);

    List<Map<String,Object>> getMarketInfoList(Integer coinId);
}
