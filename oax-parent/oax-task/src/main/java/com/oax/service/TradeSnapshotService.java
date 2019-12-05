package com.oax.service;

import java.text.ParseException;
import java.util.List;

public interface TradeSnapshotService {

    List<Integer> getMarketIds();
    boolean addTradeSnapshot(String beginTime, String endTime, Integer marketId) throws ParseException;
    boolean isBack(Integer userId);
}
