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
import com.oax.entity.front.TradeInfo;
import com.oax.mapper.front.TradeMapper;
import com.oax.service.TradeService;

/** 
* @ClassName:：TradeServiceImpl 
* @Description： 交易对实时交易 service业务层
* @author ：xiangwh  
* @date ：2018年6月5日 下午2:38:39 
*  
*/
@Service
public class TradeServiceImpl implements TradeService {
	@Autowired
	private TradeMapper mapper;
	@Autowired
	private RedisUtil redisUtil;
	
	@Override
	@DataSource(DataSourceType.SLAVE)
	public List<TradeInfo> getTradeList(Integer marketId,Integer userId) {
		
		List<TradeInfo> list =null;
		if(userId==null) {
			
			list = redisUtil.getList(RedisKeyEnum.MARKET_TRADE_LIST.getKey()+marketId, TradeInfo.class);	
			
			if (list==null||list.size()==0) {
				list = mapper.getTradeList(marketId,null);
			}
			
		}else {
			list = mapper.getTradeList(marketId,userId);
		}

		if(list!=null&&list.size()>0){
			list.stream().forEach(tradeInfo -> {
				String createTime=tradeInfo.getCreateTime();
				tradeInfo.setCreateTime(createTime.substring(createTime.length()-8));
			});
		}
		return list;
	}
	
	
	@DataSource(DataSourceType.SLAVE)
	@Override
	public List<TradeInfo> getTradeListByMarketIdFromDB(int marketId) {
		List<TradeInfo> list = mapper.getTradeList(marketId,null);
		return list;
	}

	

}
