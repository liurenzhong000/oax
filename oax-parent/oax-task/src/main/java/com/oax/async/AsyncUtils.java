package com.oax.async;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oax.common.RedisUtil;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.entity.front.TradeInfo;
import com.oax.service.MarketService;

/** 
* @ClassName:：AsyncUtils 
* @Description： 异步
* @author ：xiangwh  
* @date ：2018年7月17日 下午3:03:23 
*  
*/
@Component
public class AsyncUtils {
	@Autowired
	private RedisUtil redisUtil;
	
//	@Async
	public void marketOrdersMap(MarketService marketService,Integer marketId) {
		Map<String, Object> result = marketService.getMarketOrdersMap(marketId);
		redisUtil.setObject(RedisKeyEnum.MARKET_ORDERS_MAP.getKey()+marketId, result,-1);
	}
//	@Async
	public void MarketTradeList(MarketService marketService,Integer marketId){
		List<TradeInfo> tradeInfoList = marketService.getMarketTradeList(marketId);
		redisUtil.setList(RedisKeyEnum.MARKET_TRADE_LIST.getKey()+marketId, tradeInfoList,-1);	
	}
}
