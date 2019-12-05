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

import com.oax.Constant;
import com.oax.common.RedisUtil;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.front.Kline;
import com.oax.entity.front.MarketCategory;
import com.oax.entity.front.MarketInfo;
import com.oax.entity.front.SysConfig;
import com.oax.mapper.front.IndexPageMarketMapper;
import com.oax.mapper.front.TradeCoinMapper;
import com.oax.mapper.front.UserMaketMapper;
import com.oax.outparam.CategoryMarket;
import com.oax.service.MarketCategoryService;
import com.oax.service.TradeCoinService;
import com.oax.util.MapUtil;

/** 
* @ClassName:：TradeCoinServiceImpl 
* @Description： 交易对信息
* @author ：xiangwh  
* @date ：2018年6月5日 上午10:46:14 
*  
*/
@Service
public class TradeCoinServiceImpl implements TradeCoinService {
	@Autowired
	private TradeCoinMapper mapper;
	@Autowired
	private UserMaketMapper userMaketMapper;
	@Autowired
	private IndexPageMarketMapper indexMapper;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private MarketCategoryService  service;
	
	//获取所有交易对的交易信息
	@Override
	@DataSource(DataSourceType.SLAVE)
	public List<CategoryMarket> findList(Integer userId) {
		List<CategoryMarket> mList = new ArrayList<>();
		//获取所有的交易对给前端
		List<MarketInfo> list = redisUtil.getList(RedisKeyEnum.MARKET_LIST.getKey(), MarketInfo.class);
		List<MarketCategory> categoryList = service.findAllBySort();
		Map<String, List<MarketInfo>> resultMap = null;			
		if (list==null||list.size()==0) {
			//查询所有的交易对
			list = mapper.findList();
			redisUtil.setList(RedisKeyEnum.MARKET_LIST.getKey(), list, Constant.REDISTIME_ANHOUR);
		}
		List<Integer> userMarketIds = null;
		if (userId!=null) {
			userMarketIds = userMaketMapper.getTradeCoinListByUser(userId);
		}
		list = getNewList(userMarketIds, list);
		resultMap = MapUtil.sendMarketInfo(list);
		for (MarketCategory c : categoryList) {
			CategoryMarket cm = new CategoryMarket();
			cm.setCategoryName(c.getCategoryName());
			cm.setMarketList(resultMap.get(c.getCategoryName()));
			mList.add(cm);
		}
		return mList;
	}
	@Override
	@DataSource(DataSourceType.SLAVE)
	public List<Map<String, Object>> findIndexPageMarket() {
		List<Integer> indexMarketIds = redisUtil.getList(RedisKeyEnum.INDEX_MARKET_LIST.getKey(), Integer.class);
		if (indexMarketIds==null||indexMarketIds.size()==0) {
			indexMarketIds = indexMapper.findIndexPageMarket();
			redisUtil.setList(RedisKeyEnum.INDEX_MARKET_LIST.getKey(), indexMarketIds);
		}
		List<MarketInfo> allMarketInfoList = redisUtil.getList(RedisKeyEnum.MARKET_LIST.getKey(), MarketInfo.class);
		List<MarketInfo> indexMarketInfoList = new ArrayList<>();
		for (Integer indexMarketId : indexMarketIds) {
			for (MarketInfo marketInfo : allMarketInfoList) {
				if (indexMarketId.equals(marketInfo.getMarketId())) {
					indexMarketInfoList.add(marketInfo);
				}
			}
		}
		List<Map<String, Object>> resultList = new ArrayList<>();	
		for (MarketInfo indexMarket : indexMarketInfoList) {
			Map<String, Object> map = new HashMap<>();
			List<Kline> list = redisUtil.getList(RedisKeyEnum.MARKET_KLINE_LIST.getKey()+indexMarket.getMarketId()+"_"+60, Kline.class);
			//如果超过24条，只显示24条
			if (list!=null&&list.size()>24) {
				list = list.subList(list.size()-24, list.size());
			}
			map.put("marketCoin", indexMarket);
			map.put("kline", list);
			resultList.add(map);
		}
		return resultList;
	}
	@Override
	@DataSource(DataSourceType.SLAVE)
	public List<MarketInfo> findListFromDB() {
		List<MarketInfo> list =  mapper.findList();
		return list;
	}
	public List<MarketInfo> getNewList(List<Integer> userMarketIds,List<MarketInfo> allMarketList){
		if (userMarketIds==null||userMarketIds.size()==0) {
			return allMarketList;
		}
		for (MarketInfo marketInfo : allMarketList) {
			for (Integer marketId : userMarketIds) {
				if (marketId==marketInfo.getMarketId()) {
					marketInfo.setIsCollection(1);
					break;
				}
			}
		}
		return allMarketList;
	}
	
	
	public SysConfig getSysConfigFromList(List<SysConfig> list,String name) {
		SysConfig sysConfig = null;
		if (list!=null && list.size()!=0) {
			for (SysConfig config : list) {
				if (name.equals(config.getName())) {
					sysConfig = config;
					break;
				}
			}
		}
		return sysConfig;
	}
}
