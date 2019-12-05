package com.oax.admin.service;

import com.github.pagehelper.PageInfo;
import com.oax.entity.admin.param.SumCoinPageParam;
import com.oax.entity.admin.vo.SumCoinPageVo;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/12
 * Time: 14:18
 * 平台虚拟币统计 service
 */
public interface SumCoinService {
    /**
     * 币种id 分组计算 所有费率总和
     *
     * @param sumCoinPageParam coinId      币种id 可为null
     *                         pageNo      页码
     *                         pageSize    一页展示数
     *                         startTime   开始时间
     *                         endTime     结束时间
     * @return
     */
    PageInfo<SumCoinPageVo> getSumCoinByPageGroupByCoinId(SumCoinPageParam sumCoinPageParam);
}
