package com.oax.admin.service.activity;

import com.github.pagehelper.PageInfo;
import com.oax.admin.vo.DiceStatisticsData;
import com.oax.entity.admin.param.PageParam;

/**
 * @Auther: hyp
 * @Date: 2018/12/27 13:50
 * @Description:
 */
public interface DiceActivityService {

    DiceStatisticsData statistics(Integer userId, Integer coinId, PageParam pageParam);

    PageInfo list(Integer userId, Integer coinId, PageParam pageParam);
}
