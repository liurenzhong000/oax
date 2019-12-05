package com.oax.admin.service;

import java.util.List;

import com.oax.entity.admin.vo.SimpleMarketCategoryVo;
import com.oax.entity.front.MarketCategory;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/4
 * Time: 19:22
 */
public interface MarketCategoryService {
    /**
     * 获取所有 分区
     *
     * @return
     */
    List<MarketCategory> selectAll();

    MarketCategory findById(int categoryId);

    int add(MarketCategory marketCategory);

    int update(MarketCategory marketCategory);

    /**
     * id查询 SimpleMarketCategory
     *
     * @param marketCategoryId
     * @return
     */
    SimpleMarketCategoryVo findSimpleMarketCategoryById(Integer marketCategoryId);

    /**
     * 获取所有 SimpleMarketCategory
     * id 分区id
     * categoryName 分区名称
     *
     * @return
     */
    List<SimpleMarketCategoryVo> findAllSimpleMarketCategory();

    MarketCategory selectByCoinId(Integer coinId);
}
