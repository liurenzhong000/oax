package com.oax.admin.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.oax.admin.service.TradeMiningService;
import com.oax.common.ResultResponse;
import com.oax.entity.admin.vo.FeedBackVo;

@RestController
@RequestMapping("tradeMining")
public class TradeMiningController {

    @Autowired
    private TradeMiningService service;

    @RequestMapping("tradeFeedBackIndex")
    public ResultResponse tradeFeedBackIndex(@RequestBody FeedBackVo vo){
        if (vo.getBeginTime()==null&&vo.getEndTime()==null){
            vo = getFeedBackVo(vo);
        }
        Map<String, Object> result = service.tradeFeedBackIndex(vo);
        return new ResultResponse(true, result);
    }

    @RequestMapping("shareBonusIndex")
    public ResultResponse shareBonusIndex(@RequestBody FeedBackVo vo){
        if (vo.getBeginTime()==null&&vo.getEndTime()==null){
            vo = getFeedBackVo(vo);
        }
        PageInfo<?> pageInfo = service.shareBonusIndex(vo);
        return new ResultResponse(true, pageInfo);
    }


    private FeedBackVo getFeedBackVo(FeedBackVo vo){
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
