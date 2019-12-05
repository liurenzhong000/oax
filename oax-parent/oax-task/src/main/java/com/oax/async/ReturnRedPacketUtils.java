package com.oax.async;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.oax.service.RedPacketService;

@Component
public class ReturnRedPacketUtils {
    @Autowired
    private RedPacketService service;

    @Async
    public void upUserCoinAndInsertRecharge(Map<String,Object> params){
        try {
            service.upUserCoinAndInsertRecharge(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
