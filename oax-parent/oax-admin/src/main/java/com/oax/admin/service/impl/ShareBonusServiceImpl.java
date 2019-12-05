package com.oax.admin.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.service.ShareBonusService;
import com.oax.entity.admin.param.ShareBonusParam;
import com.oax.entity.front.ShareBonusInfo;
import com.oax.mapper.front.BenchMarkShareBonusMapper;
import com.oax.mapper.front.TradeSnapshotMapper;

@Service
public class ShareBonusServiceImpl implements ShareBonusService {
    @Autowired
    private BenchMarkShareBonusMapper mapper;
    @Autowired
    private TradeSnapshotMapper tradeSnapshotMapper;

    @Override
    public Map<String,Object> getShareBonusInfos(ShareBonusParam param) {
        //判断是否传入了时间作为条件查询
        boolean flag = false;
        if (param.getBeginTime()==null&&param.getEndTime()==null){
            flag = true;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar calendar=Calendar.getInstance(Locale.CHINA);
        String toDay = sdf.format(calendar.getTime());
        calendar.add(Calendar.DATE, 1);
        String nextDay = sdf.format(calendar.getTime());
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        String currentFriday = sdf.format(calendar1.getTime());
        calendar1.add(Calendar.DATE, -7);
        String lastFriday = sdf.format(calendar1.getTime());
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String currentMonday = sdf.format(calendar2.getTime());
        calendar2.add(Calendar.DATE, 7);
        String nextMonday = sdf.format(calendar2.getTime());
        List<ShareBonusInfo> list = mapper.getShareBonusInfoList(toDay, nextDay,lastFriday,currentFriday,currentMonday,nextMonday);
        BigDecimal totalX = tradeSnapshotMapper.getCirculationTotal();
        List<Map<String,Object>> mapList = new ArrayList<>();
        for (ShareBonusInfo shareBonusInfo : list) {
            //如果没有传时间，则取集合中的数据
            if(flag){
                Map<String,Object> map = new HashMap<>();
                map.put("coinId", shareBonusInfo.getCoinId());
                map.put("coinName", shareBonusInfo.getCoinName());
                map.put("toDayQty", shareBonusInfo.getToDayQty());
                mapList.add(map);
            }
            BigDecimal avgQty = shareBonusInfo.getToDayQty().multiply(new BigDecimal(1000000)).divide(totalX,8,BigDecimal.ROUND_FLOOR);
            shareBonusInfo.setAvgQty(avgQty);
            shareBonusInfo.setToDayQty(shareBonusInfo.getToDayQty().multiply(new BigDecimal(0.8)).setScale(8,BigDecimal.ROUND_FLOOR ));
            shareBonusInfo.setFridayQty(shareBonusInfo.getFridayQty().multiply(new BigDecimal(0.1)).setScale(8,BigDecimal.ROUND_FLOOR ));
            shareBonusInfo.setMondayQty(shareBonusInfo.getMondayQty().multiply(new BigDecimal(0.1)).setScale(8,BigDecimal.ROUND_FLOOR ));
        }
        //如果传入了时间条件模糊查询 查db
        if(!flag){
            mapList = mapper.getShareBonus(param);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("partBenchMarkShareBonusList", mapList);
        map.put("allBenchMarkShareBonusList", list);
        // 待做 非基准型交易手续费的汇总
        return map;
    }

    @Override
    public PageInfo<?> getPage(ShareBonusParam param) {
        if (param.getBeginTime()==null&&param.getEndTime()==null){
            param = this.tranform(param);
        }
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        List<Map<String,Object>> list = mapper.getPage(param);
        return new PageInfo<>(list);
    }

    private ShareBonusParam tranform(ShareBonusParam param){
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String begin = sdf1.format(cal.getTime());
        String end = sdf2.format(cal.getTime());
        try {
            param.setBeginTime(sdf3.parse(begin));
            param.setEndTime(sdf3.parse(end));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return param;
    }
}
