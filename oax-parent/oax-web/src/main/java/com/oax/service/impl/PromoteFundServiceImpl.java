package com.oax.service.impl;

import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.front.PromoteFund;
import com.oax.entity.front.PromoteFundExample;
import com.oax.entity.front.PromoteProfit;
import com.oax.mapper.front.PromoteFundMapper;
import com.oax.service.PromoteFundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromoteFundServiceImpl implements PromoteFundService{

    @Autowired
    private PromoteFundMapper promoteFundMapper;

    @DataSource(DataSourceType.MASTER)
    public  List<PromoteProfit> getPromoteList(String userId)
    {
        return promoteFundMapper.selectProfitRecord(Integer.parseInt(userId));
    }
}
