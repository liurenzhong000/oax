package com.oax.service;

import java.math.BigDecimal;
import java.util.List;

import com.oax.entity.front.Orders;
import com.oax.entity.front.TradeSnapshot;

public interface OrderFeedbackService {

    List<TradeSnapshot> getTotalOrderFeedback(String beginTime, String endTime);

    List<Orders> getCompensationOrdersList(String beginTime, String endTime, Integer marketId);

    BigDecimal getTotalWaitTradeQty(String beginTime, String endTime, Integer marketId);

    boolean addOrderFeedback(Orders orders, Integer marketId, BigDecimal totalQty, BigDecimal feeToX);
}
