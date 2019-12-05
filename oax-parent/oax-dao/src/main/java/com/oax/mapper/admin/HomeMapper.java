package com.oax.mapper.admin;


import com.oax.entity.admin.vo.HomeStatisticsVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface HomeMapper {


    HomeStatisticsVo countHomeStatistics(@Param("todayStart") Date todayStart, @Param("tomorrowStart") Date tomorrowStart);
}