package com.oax.admin.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.oax.admin.service.ITradeService;
import com.oax.common.PageResultResponse;
import com.oax.common.ResultResponse;
import com.oax.entity.admin.param.SimpleCoinParam;
import com.oax.entity.admin.param.TradePageParam;
import com.oax.entity.admin.vo.TradeFeeVo;
import com.oax.entity.admin.vo.TradePageVo;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/12
 * Time: 11:38
 * <p>
 * 成交订单 控制层
 */
@RestController
@RequestMapping("/trades")
public class TradeController {


    @Autowired
    private ITradeService tradeService;


    @PostMapping("/page")
    public ResultResponse getAllTrade(@RequestBody TradePageParam tradePageParam) {
        PageInfo<TradePageVo> pageInfo = tradeService.selectByTradePageParam(tradePageParam);
        PageResultResponse<TradePageVo> tradePageResultResponse = new PageResultResponse<>();
        BeanUtils.copyProperties(pageInfo, tradePageResultResponse);
        tradePageResultResponse.setParam(tradePageParam);
        return new ResultResponse(true, tradePageResultResponse);
    }

    @PostMapping("/fee")
    public ResultResponse getAllFee(@RequestBody SimpleCoinParam simpleCoin){
        if (simpleCoin.getBeginTime()==null&&simpleCoin.getEndTime()==null){
            simpleCoin = getSimpleCoinParam(simpleCoin);
        }

        PageInfo<TradeFeeVo> pageInfo = tradeService.countTradeFee(simpleCoin);

        PageResultResponse<TradeFeeVo> tradeFeeVoPageResultResponse = new PageResultResponse<>();

        BeanUtils.copyProperties(pageInfo,tradeFeeVoPageResultResponse);

        tradeFeeVoPageResultResponse.setParam(simpleCoin);
        return new ResultResponse(true,tradeFeeVoPageResultResponse);
    }

    private SimpleCoinParam getSimpleCoinParam(SimpleCoinParam vo){
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String beginTime = sdf1.format(cal.getTime());
        String endTime = sdf2.format(cal.getTime());
        try {
            vo.setBeginTime(sdf3.parse(beginTime));
            vo.setEndTime(sdf3.parse(endTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return vo;
    }

}
