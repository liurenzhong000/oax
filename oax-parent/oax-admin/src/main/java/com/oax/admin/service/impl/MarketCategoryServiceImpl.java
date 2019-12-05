package com.oax.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.admin.service.MarketCategoryService;
import com.oax.entity.admin.vo.SimpleMarketCategoryVo;
import com.oax.entity.front.MarketCategory;
import com.oax.mapper.front.MarketCategoryMapper;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/4
 * Time: 19:22
 */
@Service
public class MarketCategoryServiceImpl implements MarketCategoryService {


    @Autowired
    private MarketCategoryMapper marketCategoryMapper;

    @Override
    public List<MarketCategory> selectAll() {
        return marketCategoryMapper.selectAll();
    }

    @Override
    public MarketCategory findById(int categoryId) {
        return marketCategoryMapper.selectById(categoryId);
    }

    @Override
    public int add(MarketCategory marketCategory) {

        return marketCategoryMapper.insertSelective(marketCategory);
    }

    @Override
    public int update(MarketCategory marketCategory) {
        return marketCategoryMapper.updateByPrimaryKeySelective(marketCategory);
    }

    @Override
    public SimpleMarketCategoryVo findSimpleMarketCategoryById(Integer marketCategoryId) {
        return marketCategoryMapper.findSimpleMarketCategoryById(marketCategoryId);
    }

    @Override
    public List<SimpleMarketCategoryVo> findAllSimpleMarketCategory() {
        return marketCategoryMapper.selectAllSimpleMarketCategory();
    }

    @Override
    public MarketCategory selectByCoinId(Integer coinId) {
        return marketCategoryMapper.selectByCoinId(coinId);
    }

}
