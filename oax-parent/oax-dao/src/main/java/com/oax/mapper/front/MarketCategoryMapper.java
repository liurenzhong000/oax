package com.oax.mapper.front;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.oax.entity.admin.vo.SimpleMarketCategoryVo;
import com.oax.entity.front.MarketCategory;

@Mapper
public interface MarketCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MarketCategory record);

    int insertSelective(MarketCategory record);

    MarketCategory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MarketCategory record);

    int updateByPrimaryKey(MarketCategory record);

    List<MarketCategory> selectAll();

    List<MarketCategory> selectAllBySort();

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
    List<SimpleMarketCategoryVo> selectAllSimpleMarketCategory();

    /**
     * 获取 分区详情 带分区名
     *
     * @param categoryId
     * @return
     */
    MarketCategory selectById(int categoryId);

    /**
     * 通过coinId查询
     *
     * @param coinId
     * @return
     */
    MarketCategory selectByCoinId(Integer coinId);


	int updateByCoinIdSelective(MarketCategory marketCategory);

	/** 
	* @Title：selectAllBySortToAll 
	* @Description：查询所有分区包括已下架
	* @throws 
	*/
	List<MarketCategory> selectAllBySortToAll();

	BigDecimal getUsdtCnyPrice();
}