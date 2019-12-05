package com.oax.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.service.PromoteFundService;
import com.oax.entity.admin.param.profitParam;
import com.oax.entity.admin.vo.MovesayMoneyActiveListVO;
import com.oax.entity.front.PromoteProfit;
import com.oax.mapper.front.PromoteFundMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromoteFundServiceImpl implements PromoteFundService {

    @Autowired
    private PromoteFundMapper promoteFundMapper;

    public PageInfo<PromoteProfit> getPromoteListAll(profitParam profitParam)
    {
        PageHelper.startPage(profitParam.getPageNum(), profitParam.getPageSize());
        List<PromoteProfit> list = promoteFundMapper.selectProfitAll(profitParam);
        return new PageInfo<PromoteProfit>(list);
    }
}
