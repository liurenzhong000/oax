/**
 * 
 */
package com.oax.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oax.common.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.common.RedisUtil;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.front.Kline;
import com.oax.mapper.front.KlineMapper;
import com.oax.service.KlineService;
import com.oax.vo.KlineVO;

/** 
* @ClassName:：KlineServiceImpl 
* @Description： 获取k线图的service实现类
* @author ：xiangwh  
* @date ：2018年6月6日 下午2:33:05 
*  
*/
@Service
public class KlineServiceImpl implements KlineService {
	
	@Autowired
	private KlineMapper mapper;
	@Autowired
	private RedisUtil redisUtil;

	@Override
	@DataSource(DataSourceType.SLAVE)
	public List<Kline> findListByMarketId(KlineVO vo) {
		
		List<Kline> list = redisUtil.getList(RedisKeyEnum.MARKET_KLINE_LIST.getKey()+vo.getMarketId()+"_"+vo.getMinType(), Kline.class);

		/*if (list==null||list.size()==0) {
			Map<String, Object> params = new HashMap<>();
			params.put("marketId", vo.getMarketId());
			params.put("minType", vo.getMinType());
			list = mapper.findListByMarketId(params);
		}*/
		
		return list;
	}

	@Override
	@DataSource(DataSourceType.SLAVE)
	public List<List<Object>> findListByMarketIdNew(KlineVO vo){
		List<List<Object>> resultList = new ArrayList<>();
		List<Kline> list = redisUtil.getList(RedisKeyEnum.MARKET_KLINE_LIST.getKey()+vo.getMarketId()+"_"+vo.getMinType(), Kline.class);
		for (Kline item : list) {
			List<Object> klineList = new ArrayList<>(6);
			klineList.add(item.getOpen().stripTrailingZeros());
			klineList.add(item.getClose().stripTrailingZeros());
			klineList.add(item.getHigh().stripTrailingZeros());
			klineList.add(item.getLow().stripTrailingZeros());
			klineList.add(item.getQty().stripTrailingZeros());
			klineList.add(DateHelper.format(item.getCreateTime()));
			resultList.add(klineList);
		}
		return resultList;
	}

	@Override
	@DataSource(DataSourceType.SLAVE)
	public List<Kline> findListByMarketIdGtOrLt(int marketId) {
		return mapper.findListByMarketIdGtOneDay(marketId);
	}
	
	@Override
	@DataSource(DataSourceType.SLAVE)
	public List<Kline> findListByMarketIdFromDB(KlineVO vo) {
		Map<String, Object> params = new HashMap<>();
		params.put("marketId", vo.getMarketId());
		params.put("minType", vo.getMinType());
		List<Kline> list = mapper.findListByMarketId(params);
		return list;
	}

}
