package com.oax.admin.service.bonus;

import com.github.pagehelper.PageInfo;
import com.oax.entity.admin.param.PageParam;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: hyp
 * @Date: 2019/1/11 18:12
 * @Description:
 */
public interface BonusLogService {

    void exportExcel(BigDecimal bhbUsdtRatio, BigDecimal allBonus, Integer reachCount, Date startTime, Date endTime);

    PageInfo pageByRedis(PageParam pageParam);
}
