package com.oax.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oax.common.enums.CoinEnum;
import com.oax.entity.front.Trade;
import com.oax.entity.front.TradeFeedBack;
import com.oax.entity.front.TradeSnapshot;
import com.oax.entity.front.UserCoin;
import com.oax.mapper.front.TradeFeedBackLogMapper;
import com.oax.service.TradeFeedbackService;

@Service
public class TradeFeedbackServiceImpl implements TradeFeedbackService {
    @Autowired
    private TradeFeedBackLogMapper mapper;
    @Override
    public List<TradeSnapshot> getTradeSnapshotByYesterday(String minDate, String maxDate) {
        List<TradeSnapshot> list = mapper.getTradeSnapshotByYesterday(minDate,maxDate);
        return list;
    }

    @Override
    public List<Trade> getWaitFeedBackTradeList(TradeSnapshot tradeSnapshot) {
        List<Trade> list = mapper.getWaitFeedBackTradeList(tradeSnapshot);
        return list;
    }

    @Override
    @Transactional
    public boolean addTradeFeedbackLogs(Trade trade, TradeSnapshot tradeSnapshot) throws Exception{
        //添加回馈记录
        TradeFeedBack log = new TradeFeedBack();
        log.setSnapshotId(tradeSnapshot.getId());
        log.setTradeId(trade.getId());
        log.setUserId(trade.getUserId());
        BigDecimal qtyRate = trade.getQty().divide(tradeSnapshot.getTradeQty(),8,BigDecimal.ROUND_FLOOR);
        BigDecimal qty = tradeSnapshot.getFeeToX().multiply(new BigDecimal(0.8).multiply(qtyRate)).setScale(8, BigDecimal.ROUND_FLOOR);
        log.setQty(qty);
        log.setCreateTime(new Date());
        log.setTradeQty(trade.getQty());
        Integer insertRow = mapper.insert(log);
        if (insertRow!=null&&insertRow>0){
            //修改交易记录状态为1 表示已回馈
            mapper.updateTrade(trade.getId());
            //修改用户X币资产数据
            UserCoin userCoin = new UserCoin();
            userCoin.setUserId(trade.getUserId());
            //X币id=3
            userCoin.setCoinId(CoinEnum.TOKEN_X.getValue());
            userCoin.setBanlance(qty);
            Integer count = mapper.selectUserCoin(userCoin);
            if (count!=null&&count>0){
                //如果该用户有X币资产  修改资产数据即可
                mapper.updateUserCoinByUserIdAndCoinId(userCoin);
            }else{
                //如果该用户没有X币资产 则添加用户X币资产数据
                userCoin.setFreezingBanlance(BigDecimal.ZERO);
                userCoin.setCreateTime(new Date());
                userCoin.setUpdateTime(new Date());
                //插入x币资产
                mapper.insertUserCoin(userCoin);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean updateStatus(Integer id, Integer status) {
        Integer changeRows = mapper.updateStatus(id,status);
        if (changeRows==null||changeRows==0){
            return false;
        }
        return true;
    }
}
