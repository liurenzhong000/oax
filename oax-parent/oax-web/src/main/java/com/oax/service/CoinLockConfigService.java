package com.oax.service;

import java.util.List;
import java.util.Map;

import com.oax.entity.front.CoinLockConfig;

public interface CoinLockConfigService {
    public Map<String,Object> getCoinLockConfig();

    List<CoinLockConfig> selectAll();
}
