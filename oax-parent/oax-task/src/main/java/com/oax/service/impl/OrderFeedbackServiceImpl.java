package com.oax.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oax.common.enums.CoinEnum;
import com.oax.entity.front.OrderFeedback;
import com.oax.entity.front.Orders;
import com.oax.entity.front.TradeSnapshot;
import com.oax.entity.front.UserCoin;
import com.oax.mapper.front.OrderFeedbackMapper;
import com.oax.mapper.front.TradeFeedBackLogMapper;
import com.oax.service.OrderFeedbackService;

@Service
public class OrderFeedbackServiceImpl implements OrderFeedbackService{
    @Autowired
    private OrderFeedbackMapper mapper;
    @Autowired
    private TradeFeedBackLogMapper tradeFeedBackLogMapper;

    @Override
    public List<TradeSnapshot> getTotalOrderFeedback(String beginTime, String endTime) {
        return mapper.getTotalOrderFeedback(beginTime,endTime);
    }

    //获取补偿挂单
    @Override
    public List<Orders> getCompensationOrdersList(String beginTime, String endTime, Integer marketId) {
        return mapper.getCompensationOrdersList(beginTime,endTime,marketId);
    }

    @Override
    public BigDecimal getTotalWaitTradeQty(String beginTime, String endTime, Integer marketId) {
        return mapper.getTotalWaitTradeQty(beginTime,endTime,marketId);
    }

    @Transactional
    @Override
    public boolean addOrderFeedback(Orders orders, Integer marketId, BigDecimal totalQty, BigDecimal feeToX) {
        BigDecimal xQty = orders.getQty().divide(totalQty,8,BigDecimal.ROUND_FLOOR).multiply(feeToX);
        OrderFeedback orderFeedback = new OrderFeedback();
        orderFeedback.setOrderId(orders.getId());
        orderFeedback.setMarketId(marketId);
        orderFeedback.setQty(xQty);
        orderFeedback.setUserId(orders.getUserId());
        orderFeedback.setCreateTime(new Date());
        orderFeedback.setOrderQty(orders.getQty());
        //插入补偿订单回馈数据
        Integer count1 = mapper.insertOrderFeedback(orderFeedback);
        //如果插入成功  1.修改orders表中 isfeedback为1已反馈   2.修改用户X币资产数据
        if (count1!=null&&count1>0){
            mapper.updateOrdersIsFeedback(orders.getId());
            //修改用户X币资产数据
            UserCoin userCoin = new UserCoin();
            userCoin.setUserId(orders.getUserId());
            //X币id=3
            userCoin.setCoinId(CoinEnum.TOKEN_X.getValue());
            userCoin.setBanlance(xQty);
            Integer count = tradeFeedBackLogMapper.selectUserCoin(userCoin);
            if (count!=null&&count>0){
                //如果该用户有X币资产  修改资产数据即可
                tradeFeedBackLogMapper.updateUserCoinByUserIdAndCoinId(userCoin);
            }else{
                //如果该用户没有X币资产 则添加用户X币资产数据
                userCoin.setFreezingBanlance(BigDecimal.ZERO);
                userCoin.setCreateTime(new Date());
                userCoin.setUpdateTime(new Date());
                //插入x币资产
                tradeFeedBackLogMapper.insertUserCoin(userCoin);
            }
            return true;
        }
        return false;
    }

}
