package com.oax.service;

import java.util.List;

import com.oax.entity.front.MarketCoinInfo;
import com.oax.entity.front.Orders;

public interface OrderService {
    List<MarketCoinInfo> selectAutoAddMarket();

    List<Orders> selectOrdersByTimeAndMarketId(Integer id,String userId, String beginTime, String endTime);

    List<Orders> selectAutoOrders(Integer marketId,String oaxApiUserId,Integer orderId);

    Integer deleteOrders(Integer marketId,String oaxApiUserId,String endTime);

    Integer selectLastOrderId();
}
