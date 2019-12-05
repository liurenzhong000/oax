package com.oax.admin.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.oax.entity.admin.vo.FeedBackVo;

public interface TradeMiningService {

    public Map<String,Object> tradeFeedBackIndex(FeedBackVo vo);
    public PageInfo<?> shareBonusIndex(FeedBackVo vo);
}
