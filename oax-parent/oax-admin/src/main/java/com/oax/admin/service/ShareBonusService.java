package com.oax.admin.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.oax.entity.admin.param.ShareBonusParam;

public interface ShareBonusService {
    public Map<String,Object> getShareBonusInfos(ShareBonusParam param);
    public PageInfo<?> getPage(ShareBonusParam param);
}
