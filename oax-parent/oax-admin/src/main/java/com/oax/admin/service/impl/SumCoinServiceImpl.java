package com.oax.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.service.SumCoinService;
import com.oax.entity.admin.param.SumCoinPageParam;
import com.oax.entity.admin.vo.SumCoinPageVo;
import com.oax.mapper.front.SumCoinMapper;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/12
 * Time: 14:19
 */
@Service
public class SumCoinServiceImpl implements SumCoinService {
    @Autowired
    private SumCoinMapper sumCoinMapper;

    @Override
    public PageInfo<SumCoinPageVo> getSumCoinByPageGroupByCoinId(SumCoinPageParam sumCoinPageParam) {
        PageHelper.startPage(sumCoinPageParam.getPageNum(), sumCoinPageParam.getPageSize());
        List<SumCoinPageVo> sumCoinPageVoList = sumCoinMapper.getSumCoinByPageGroupByCoinId(sumCoinPageParam);

        return new PageInfo<>(sumCoinPageVoList);
    }
}
