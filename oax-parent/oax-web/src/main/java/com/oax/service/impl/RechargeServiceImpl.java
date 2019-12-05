package com.oax.service.impl;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.admin.param.RechargesOrWithdrawParam;
import com.oax.entity.front.RechargeResult;
import com.oax.mapper.front.RechargeMapper;
import com.oax.service.IRechargeService;
import com.oax.vo.RechargeListVO;

@Service
public class RechargeServiceImpl implements IRechargeService {

    @Autowired
    private RechargeMapper rechargeMapper;

    @DataSource(DataSourceType.SLAVE)
    public PageInfo<RechargeResult> getByUserIdRecharge(RechargeListVO vo) {
    	PageHelper.startPage(vo.getPageIndex(), vo.getPageSize());
        List<RechargeResult> list = new ArrayList<>();
        if (vo.getUserId() != null){
        	RechargesOrWithdrawParam rechargesOrWithdrawParam =new RechargesOrWithdrawParam();
        	rechargesOrWithdrawParam.setUserId(vo.getUserId());
        	rechargesOrWithdrawParam.setCoinName(vo.getCoinName());
        	
            list = rechargeMapper.getByUserId(rechargesOrWithdrawParam);
        }
        return new PageInfo<> (list);
    }
}
