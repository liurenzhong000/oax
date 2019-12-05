package com.oax.admin.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.service.TradeFeedBackService;
import com.oax.entity.admin.vo.FeedBackVo;
import com.oax.mapper.front.OrderFeedbackMapper;
import com.oax.mapper.front.TradeFeedBackLogMapper;
import com.oax.mapper.front.TradeSnapshotMapper;
@Service
public class TradeFeedBackServiceImpl implements TradeFeedBackService {
    @Autowired
    private TradeFeedBackLogMapper mapper;
    @Autowired
    private OrderFeedbackMapper orderFeedbackMapper;
    @Autowired
    private TradeSnapshotMapper tradeSnapshotMapper;
    @Override
    public PageInfo<Map<String, Object>> selectAllTradeFeedBack(FeedBackVo vo) {
        PageHelper.startPage(vo.getPageNo(), vo.getPageSize());
        List<Map<String, Object>> list = mapper.selectAll(vo);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<Map<String, Object>> selectAllOrderFeedBack(FeedBackVo vo) {
        PageHelper.startPage(vo.getPageNo(), vo.getPageSize());
        List<Map<String, Object>> list = orderFeedbackMapper.selectAll(vo);
        return new PageInfo<>(list);
    }

    @Override
    public Map<String, Object> collectFeedBack(FeedBackVo vo) {
        Map<String, Object> map = mapper.collectFeedBack(vo);
        //x币总流通量
        BigDecimal circulationTotal=tradeSnapshotMapper.getCirculationTotal();
        map.put("xTokenTotalCirculation", circulationTotal);
        return map;
    }

    @Override
    public Map<String, Object> collectToday() {
        Map<String,Object> map = new HashMap<>();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String beginTime = sdf.format(c.getTime());
        BigDecimal feeToX = tradeSnapshotMapper.getFeedBackToDay(beginTime);
        BigDecimal tradeFeeToX = feeToX.multiply(new BigDecimal(0.8)).setScale(8, BigDecimal.ROUND_FLOOR);
        BigDecimal orderFeeToX = feeToX.multiply(new BigDecimal(0.2)).setScale(8, BigDecimal.ROUND_FLOOR);
        map.put("feeToX", feeToX);
        map.put("tradeFeeToX", tradeFeeToX);
        map.put("orderFeeToX", orderFeeToX);
        List<Map<String,Object>> miningMarketList = tradeSnapshotMapper.MiningMarket();
        map.put("miningMarketList", miningMarketList);
        return map;
    }
}
