package com.oax.admin.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.service.TradeMiningService;
import com.oax.entity.admin.vo.FeedBackVo;
import com.oax.mapper.front.BenchMarkShareBonusMapper;
import com.oax.mapper.front.TradeFeedBackLogMapper;
import com.oax.mapper.front.TradeSnapshotMapper;

@Service
public class TradeMiningServiceImpl implements TradeMiningService {

    @Autowired
    private TradeFeedBackLogMapper tradeFeedBackLogMapper;
    @Autowired
    private TradeSnapshotMapper tradeSnapshotMapper;
    @Autowired
    private BenchMarkShareBonusMapper benchMarkShareBonusMapper;

    @Override
    public Map<String, Object> tradeFeedBackIndex(FeedBackVo vo) {
        Map<String, Object> map = tradeFeedBackLogMapper.collectFeedBack(vo);
        //x币总流通量
        BigDecimal circulationTotal=tradeSnapshotMapper.getCirculationTotal();
        map.put("xTokenTotalCirculation", circulationTotal);
        return map;
    }

    @Override
    public PageInfo<Map<String, Object>> shareBonusIndex(FeedBackVo vo) {
        PageHelper.startPage(vo.getPageNo(), vo.getPageSize());
        List<Map<String,Object>> list = benchMarkShareBonusMapper.ShareBonusIndex(vo);
        return new PageInfo<>(list);
    }
}
