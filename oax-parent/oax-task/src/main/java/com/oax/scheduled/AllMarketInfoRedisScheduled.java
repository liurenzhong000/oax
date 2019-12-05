package com.oax.scheduled;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.oax.common.RedisUtil;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.entity.front.MarketInfo;
import com.oax.service.MarketService;

/**
* @ClassName:：WebSocketRedisScheduled 
* @Description： 所有交易对综合信息的缓存
* @author ：xiangwh  
* @date ：2018年7月16日 上午10:59:20 
*  
*/
@Component
public class AllMarketInfoRedisScheduled {
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private MarketService marketService;
	
	@Scheduled(fixedRate= 1000 * 10)
	@Async
	public void allMarketInfo() {
		List<MarketInfo> list = marketService.allMarketInfo();
		redisUtil.setList(RedisKeyEnum.MARKET_LIST.getKey(), list,-1);
	}
}
