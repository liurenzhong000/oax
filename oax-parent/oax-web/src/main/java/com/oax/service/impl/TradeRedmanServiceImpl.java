package com.oax.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.admin.param.TradeRedmanParam;
import com.oax.entity.front.TradeRedman;
import com.oax.mapper.front.TradeRedmanMapper;
import com.oax.service.TradeRedmanService;

@Service
public class TradeRedmanServiceImpl implements TradeRedmanService{

    @Autowired
    private TradeRedmanMapper tradeRedmanMapper;

    @DataSource(DataSourceType.SLAVE)
    @Override
    public PageInfo getPage(TradeRedmanParam param) {
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar calendar=Calendar.getInstance(Locale.CHINA);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String currentMonday = sdf.format(calendar.getTime());
        calendar.add(Calendar.DATE, 7);
        String nextMonday = sdf.format(calendar.getTime());
        List<TradeRedman> list = tradeRedmanMapper.getList(currentMonday,nextMonday);
        return new PageInfo(list);
    }
}
