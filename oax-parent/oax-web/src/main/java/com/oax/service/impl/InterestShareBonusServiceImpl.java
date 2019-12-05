package com.oax.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.mapper.front.InterestShareBonusMapper;
import com.oax.service.InterestShareBonusService;

@Service
public class InterestShareBonusServiceImpl implements InterestShareBonusService {
    @Autowired
    private InterestShareBonusMapper mapper;

    @DataSource(DataSourceType.SLAVE)
    @Override
    public List<Map<String, Object>> myIncome(Integer userId) {
        return mapper.myIncome(userId);
    }
}
