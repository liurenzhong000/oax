/**
 * 
 */
package com.oax.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.common.RedisUtil;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.front.MarketCategory;
import com.oax.mapper.front.MarketCategoryMapper;
import com.oax.service.MarketCategoryService;

/** 
* @ClassName:：MarketCategoryServiceImpl 
* @Description： 查询市场分区信息
* @author ：xiangwh  
* @date ：2018年6月5日 下午6:14:16 
*  
*/
@Service
public class MarketCategoryServiceImpl implements MarketCategoryService {

	@Autowired
	private MarketCategoryMapper mapper;
	@Autowired
	private RedisUtil redisUtil;
	@Override
	@DataSource(DataSourceType.SLAVE)
	public List<MarketCategory> findAllBySort() {
		List<MarketCategory> list = redisUtil.getList(RedisKeyEnum.MARKET_CATEGORY_LIST.getKey(), MarketCategory.class);
		if (list==null || list.size()==0) {
			list = mapper.selectAllBySort();
			redisUtil.setList(RedisKeyEnum.MARKET_CATEGORY_LIST.getKey(), list,-1);
		}
		return list;
	}
	
	@Override
	@DataSource(DataSourceType.SLAVE)
	public List<MarketCategory> findAllBySortToAll() {
		List<MarketCategory> list = redisUtil.getList(RedisKeyEnum.MARKET_CATEGORY_ALLLIST.getKey(), MarketCategory.class);
		if (list==null || list.size()==0) {
			list = mapper.selectAllBySortToAll();
			redisUtil.setList(RedisKeyEnum.MARKET_CATEGORY_ALLLIST.getKey(), list,-1);
		}
		return list;
	}

}
