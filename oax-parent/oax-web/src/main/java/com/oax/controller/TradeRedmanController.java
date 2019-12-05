package com.oax.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.oax.common.ResultResponse;
import com.oax.entity.admin.param.TradeRedmanParam;
import com.oax.service.TradeRedmanService;

@RestController
@RequestMapping("tradeRedman")
public class TradeRedmanController {

    @Autowired
    private TradeRedmanService tradeRedmanService;

    @PostMapping("index")
    public ResultResponse getPage(@RequestBody TradeRedmanParam param){
        PageInfo<?> page = tradeRedmanService.getPage(param);
        return new ResultResponse(true, page);
    }
}
