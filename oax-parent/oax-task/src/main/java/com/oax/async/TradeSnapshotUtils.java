package com.oax.async;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.oax.service.TradeSnapshotService;

@Component
public class TradeSnapshotUtils {
    @Autowired
    TradeSnapshotService service;

    @Async
    public void  addTradeSnapshot(String beginTime,String endTime,Integer marketId) throws ParseException {
        service.addTradeSnapshot(beginTime,endTime,marketId);
    }

}
