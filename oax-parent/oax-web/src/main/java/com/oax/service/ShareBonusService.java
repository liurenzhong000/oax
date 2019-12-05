package com.oax.service;

import java.util.List;
import java.util.Map;

import com.oax.entity.front.ShareBonusInfo;

public interface ShareBonusService {

    public List<ShareBonusInfo> getShareBonusInfoList();
    public List<Map<String,Object>> getMarketShareBonusInfoList();
}
