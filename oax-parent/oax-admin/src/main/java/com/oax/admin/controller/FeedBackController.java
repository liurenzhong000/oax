package com.oax.admin.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.oax.admin.service.TradeFeedBackService;
import com.oax.common.ResultResponse;
import com.oax.entity.admin.vo.FeedBackVo;

@RestController
@RequestMapping("/feedBack")
public class FeedBackController {
    @Autowired
    private TradeFeedBackService tradeFeedBackService;

    @PostMapping("/trade")
    public ResultResponse getTradeFeedBack(@RequestBody FeedBackVo vo){
        if (vo.getBeginTime()==null&&vo.getEndTime()==null){
            vo = this.getFeedBackVo(vo,1);
        }
        PageInfo<?> pageInfo = tradeFeedBackService.selectAllTradeFeedBack(vo);
        return new ResultResponse(true, pageInfo);
    }

    @PostMapping("/order")
    public ResultResponse getOrderFeedBack(@RequestBody FeedBackVo vo){
        if (vo.getBeginTime()==null&&vo.getEndTime()==null){
            vo = this.getFeedBackVo(vo,2);
        }
        PageInfo<?> pageInfo = tradeFeedBackService.selectAllOrderFeedBack(vo);
        return new ResultResponse(true, pageInfo);
    }

    private FeedBackVo getFeedBackVo(FeedBackVo vo,int type){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2= new SimpleDateFormat("HH");
        Calendar calendar = Calendar.getInstance();
        Integer currentHour = Integer.parseInt(sdf2.format(calendar.getTime()));
        if (type==1||currentHour<13){
            calendar.add(Calendar.DATE, -1);
        }
        Date date = calendar.getTime();
        String beginTime = sdf.format(date)+" 00:00:00";
        String endTime = sdf.format(date)+" 23:59:59";
        try {
            vo.setBeginTime(sdf1.parse(beginTime));
            vo.setEndTime(sdf1.parse(endTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return vo;
    }

    @PostMapping("/collect")
    public ResultResponse collectFeedBack(@RequestBody FeedBackVo vo){
        Map<String,Object> result = tradeFeedBackService.collectFeedBack(vo);
        return new ResultResponse(true, result);
    }

    @PostMapping("/collectTodayAndMiningMarket")
    public ResultResponse collectToday(){
        Map<String,Object> result = tradeFeedBackService.collectToday();
        return new ResultResponse(true, result);
    }

}
