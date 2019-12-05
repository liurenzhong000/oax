package com.oax.scheduled;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.oax.async.AsyncUtils;
import com.oax.entity.front.Market;
import com.oax.service.MarketService;

/**
* @ClassName:：MarketOrdersRedisScheduled 
* @Description： 交易对的委托深度缓存
* @author ：xiangwh  
* @date ：2018年7月16日 上午11:37:35 
*  
*/
@Component
@Slf4j
public class MarketOrdersAndTradesRedisScheduled {
	@Autowired
	private AsyncUtils asyncUtils;
	@Autowired
	private MarketService marketService;
	
	@Scheduled(fixedRate= 1000* 60*60*24*365)
	@Async
	public void marketOrders() {
		while (true){
			try {
				log.info("同步交易委托记录=======================");
				List<Market> list = marketService.allMarketsOnShelf();
				for (Market market : list) {
					asyncUtils.marketOrdersMap(marketService, market.getId());
					asyncUtils.MarketTradeList(marketService, market.getId());
				}
				list = null;
			} catch (Exception e) {
				log.info("同步交易委托记录:",e);
			}

//			try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}

	}
}
