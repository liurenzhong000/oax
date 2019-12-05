package com.oax.scheduled;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.oax.async.ReturnRedPacketUtils;
import com.oax.service.RedPacketService;

/**
 *过期红包退回
 */
@Component
public class ReturnRedPacketScheduled {
    @Autowired
    private RedPacketService redPacketService;
    @Autowired
    private ReturnRedPacketUtils utils;
    @Scheduled(cron="0 0 0/1 * * ?")
    @Async
    public void returnOverdueRedPacket(){
        List<Map<String,Object>> redPacketList = redPacketService.findOverdueRedPacket();
        if(redPacketList!=null&&redPacketList.size()!=0){
            for (Map<String, Object> params : redPacketList) {
                utils.upUserCoinAndInsertRecharge(params);
            }
        }

    }

}
