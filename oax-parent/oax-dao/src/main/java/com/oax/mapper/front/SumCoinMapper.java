package com.oax.mapper.front;

import java.util.List;

import com.oax.entity.admin.param.SumCoinPageParam;
import com.oax.entity.admin.vo.SumCoinPageVo;
import com.oax.entity.front.SumCoin;

public interface SumCoinMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SumCoin record);

    int insertSelective(SumCoin record);

    SumCoin selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SumCoin record);

    int updateByPrimaryKey(SumCoin record);

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
    List<SumCoinPageVo> getSumCoinByPageGroupByCoinId(SumCoinPageParam sumCoinPageParam);
}