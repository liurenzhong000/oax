/**
 * 
 */
package com.oax.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.front.Market;
import com.oax.entity.front.MarketCategory;
import com.oax.entity.front.MarketCategoryInfo;
import com.oax.entity.front.MarketCategoryResult;
import com.oax.mapper.front.MarketMapper;
import com.oax.service.MarketCategoryService;
import com.oax.service.MarketService;

/** 
* @ClassName:：MarketServiceImpl 
* @Description： 交易对service实现类
* @author ：xiangwh  
* @date ：2018年6月6日 下午10:44:53 
*  
*/
@Service
public class MarketServiceImpl implements MarketService{
	@Autowired
	private MarketMapper mapper;
	@Autowired
	private MarketCategoryService  service;
	@Override
	@DataSource(DataSourceType.SLAVE)
	public List<Market> selectAll() {
//		return mapper.selectListAll();
		return mapper.allMarketsOnShelf();
	}

	@Override
	@DataSource(DataSourceType.SLAVE)
	public List<Map<String, Object>> getMarketList() {
		List<Map<String, Object>> list = mapper.getMarketList();
		return list;
	}

	@Override
	@DataSource(DataSourceType.SLAVE)
	public List<MarketCategoryResult> getMarketCategoryInfo() {
		List<MarketCategoryResult> resultList = new ArrayList<>();
		//获取所有交易对信息
		List<MarketCategoryInfo> marketCategoryInfoList  = mapper.getMarketCategoryInfo();
		//获取所有市场分区信息
		List<MarketCategory> marketCategoryList = service.findAllBySortToAll();
		Map<String, List<MarketCategoryInfo>> map = getMap(marketCategoryInfoList);
		for (MarketCategory marketCategory : marketCategoryList) {
			MarketCategoryResult mr = new MarketCategoryResult();
			mr.setCategoryName(marketCategory.getCategoryName());
			mr.setMarketCategoryList(map.get(marketCategory.getCategoryName()));
			resultList.add(mr);
		}
		return resultList;
	}
	
	public Map<String, List<MarketCategoryInfo>> getMap(List<MarketCategoryInfo>list){
		Map<String, List<MarketCategoryInfo>> map = new HashMap<>();
		for (MarketCategoryInfo info : list) {
			String categoryName = info.getCategoryName();
			if (map.containsKey(categoryName)) {
				map.get(categoryName).add(info);
			}else {
				List<MarketCategoryInfo> l = new ArrayList<>();
				l.add(info);
				map.put(categoryName, l);		
			}
		}		
		return map;
	}
	
}
