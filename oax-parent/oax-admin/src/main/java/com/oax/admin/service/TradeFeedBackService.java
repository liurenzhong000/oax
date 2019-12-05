package com.oax.admin.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.oax.entity.admin.vo.FeedBackVo;

public interface TradeFeedBackService {

    public PageInfo<?> selectAllTradeFeedBack(FeedBackVo feedBackVo);

    public PageInfo<?> selectAllOrderFeedBack(FeedBackVo vo);

    Map<String,Object> collectFeedBack(FeedBackVo vo);

    Map<String,Object> collectToday();
}
