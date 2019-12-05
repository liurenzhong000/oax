package com.oax.admin.service.impl;

import com.oax.common.DateHelper;
import com.oax.common.RedisUtil;
import com.oax.common.json.JsonHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.admin.service.HomeService;
import com.oax.entity.admin.vo.HomeStatisticsVo;
import com.oax.mapper.admin.HomeMapper;

import javax.xml.crypto.Data;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/18
 * Time: 17:37
 */
@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private HomeMapper homeMapper;

    @Autowired
    private RedisUtil redisUtil;

    //首页数据缓存redis key
    private final static String KEY = "admin_home_data";
    //每10分钟才能触发统计查询sql
    private final static String WAIT_KEY = "admin_home_data_wait";

    @Override
    public HomeStatisticsVo userandOrderStatistics() {
        //缓存10分钟
        Date todayStart = DateHelper.getTodayStart();
        Date tomorrowStart = DateHelper.getTomorrowStart();
        HomeStatisticsVo homeStatisticsVo;
        String jsonStr = redisUtil.getString(KEY);
        if (StringUtils.isBlank(jsonStr)){
            String waitStr = redisUtil.getString(WAIT_KEY);
            if(StringUtils.isBlank(waitStr)) {
                redisUtil.setString(WAIT_KEY, "60", 60);
                homeStatisticsVo = homeMapper.countHomeStatistics(todayStart, tomorrowStart);
                homeStatisticsVo.setLastUpdateDate(new Date());
                jsonStr = JsonHelper.writeValueAsString(homeStatisticsVo);
                redisUtil.setString(KEY, jsonStr, 3600);//缓存1小时
            }
        }
        homeStatisticsVo = JsonHelper.readValue(jsonStr, HomeStatisticsVo.class);
        return homeStatisticsVo;
    }

}
