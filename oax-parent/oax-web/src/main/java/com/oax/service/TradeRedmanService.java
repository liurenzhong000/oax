package com.oax.service;

import com.github.pagehelper.PageInfo;
import com.oax.entity.admin.param.TradeRedmanParam;

public interface TradeRedmanService {

    public PageInfo getPage(TradeRedmanParam param);
}
