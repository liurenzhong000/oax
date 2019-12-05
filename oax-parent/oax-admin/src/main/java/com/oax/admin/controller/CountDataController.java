package com.oax.admin.controller;


import com.oax.admin.service.count.CountDataService;
import com.oax.common.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/CountData")
public class CountDataController
{
    @Autowired
    private CountDataService countDataService;

    @PostMapping("/getCountDataBHBorBCB")
    public ResultResponse getCountDataBHBorBCB(int coin_id){
        BigDecimal count =  countDataService.CountBorC(coin_id);
        return new ResultResponse(true, count);
    }

}
