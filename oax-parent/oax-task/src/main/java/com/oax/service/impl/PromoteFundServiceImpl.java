package com.oax.service.impl;

import com.oax.entity.front.MovesayMoneyActiveListExample;
import com.oax.entity.front.PromoteFund;
import com.oax.entity.front.PromoteFundExample;
import com.oax.mapper.front.PromoteFundMapper;
import com.oax.service.PromoteFundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromoteFundServiceImpl implements PromoteFundService {

    @Autowired
    private PromoteFundMapper promoteFundMapper;

    public List<PromoteFund> getPromoteByUser(Integer user_id)
    {
        PromoteFundExample promoteFundExample=new PromoteFundExample();
        PromoteFundExample.Criteria criteria=promoteFundExample.createCriteria();
        criteria.andUseridEqualTo(user_id);
        criteria.andStatusEqualTo(1);
        return promoteFundMapper.selectByExample(promoteFundExample);
    }


}
