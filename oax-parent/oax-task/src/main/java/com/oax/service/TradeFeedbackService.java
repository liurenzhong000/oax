package com.oax.service;

import java.util.List;

import com.oax.entity.front.Trade;
import com.oax.entity.front.TradeSnapshot;

public interface TradeFeedbackService {

    List<TradeSnapshot> getTradeSnapshotByYesterday(String minDate, String maxDate);

    List<Trade> getWaitFeedBackTradeList(TradeSnapshot tradeSnapshot);

    boolean addTradeFeedbackLogs(Trade trade, TradeSnapshot tradeSnapshot) throws Exception;

    boolean updateStatus(Integer id, Integer status);
}
