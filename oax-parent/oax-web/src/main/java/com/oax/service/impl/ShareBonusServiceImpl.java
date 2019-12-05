package com.oax.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.common.RedisUtil;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.front.ShareBonusInfo;
import com.oax.mapper.front.BenchMarkShareBonusMapper;
import com.oax.service.ShareBonusService;

@Service
public class ShareBonusServiceImpl implements ShareBonusService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private BenchMarkShareBonusMapper mapper;

    @DataSource(DataSourceType.SLAVE)
    @Override
    public List<ShareBonusInfo> getShareBonusInfoList() {
        List<ShareBonusInfo> shareBonusList = redisUtil.getList(RedisKeyEnum.SHAREBONUS_LIST.getKey(), ShareBonusInfo.class);
        return shareBonusList;
    }

    @DataSource(DataSourceType.SLAVE)
    @Override
    public List<Map<String, Object>> getMarketShareBonusInfoList() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar calendar = Calendar.getInstance();
        String beginTime = sdf.format(calendar.getTime());
        calendar.add(Calendar.DATE, 1);
        String endTime = sdf.format(calendar.getTime());
        List<Map<String,Object>> marKetShareBonusList = mapper.getMarKetShareBonusList(beginTime,endTime);
        return marKetShareBonusList;
    }
}
