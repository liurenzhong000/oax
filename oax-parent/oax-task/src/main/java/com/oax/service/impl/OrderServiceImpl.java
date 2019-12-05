package com.oax.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.entity.front.MarketCoinInfo;
import com.oax.entity.front.Orders;
import com.oax.mapper.front.MarketMapper;
import com.oax.mapper.front.OrdersMapper;
import com.oax.service.OrderService;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private MarketMapper marketMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    @Override
    public List<MarketCoinInfo> selectAutoAddMarket() {
        List<MarketCoinInfo> marketCoinList= marketMapper.selectAutoAddMarket();
        return marketCoinList;
    }

    @Override
    public List<Orders> selectOrdersByTimeAndMarketId(Integer marketId,String userId, String beginTime, String endTime) {
        List<Orders> orderList=ordersMapper.getOrderByTimeAndMarketId(marketId,Integer.parseInt(userId),beginTime,endTime);
        return orderList;
    }

    @Override
    public List<Orders> selectAutoOrders(Integer marketId,String userId,Integer orderId) {
        List<Orders> orderList=ordersMapper.getAutoOrders(marketId,Integer.parseInt(userId),orderId);
        return orderList;
    }

    @Override
    public Integer deleteOrders(Integer marketId,String userId,String endTime) {
        Integer counts=ordersMapper.deleteOrders(marketId,Integer.parseInt(userId),endTime);
        return counts;
    }

    @Override
    public Integer selectLastOrderId() {
        Integer orderId=ordersMapper.selectLastOrderId();
        return orderId;
    }
}