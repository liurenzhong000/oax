package com.oax.scheduled;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oax.common.RedisUtil;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.entity.front.Kline;
import com.oax.entity.front.Market;
import com.oax.service.BenchMarkShareBonusService;
import com.oax.service.KlineService;
import com.oax.service.MarketService;
import com.oax.util.MapUtil;

/**
 * @ClassName:：KlineTask @Description： k线定时任务
 * @author ：xiangwh
 * @date ：2018年7月3日 下午3:09:27
 * 
 */
@Component
public class KlineRedisTask {
	@Autowired
	private KlineService klineService;
	@Autowired
	private MarketService marketService;
	@Autowired
	private RedisUtil redisUtil;

	@PostConstruct
	public void setRedisByKline() {
		List<Market> marketList = marketService.selectAll();
		System.out.println("marketList="+marketList);
		for (Market market : marketList) {
			Map<String, Object> params = new HashMap<>();
			params.put("marketId", market.getId());
			// 1分钟,5分钟,10分钟,30分钟,60分钟,240分钟,1440分钟}
			int[] minTypeArr = { 1, 5, 10, 30, 60, 240, 1440 };
			for (int i = 0; i < minTypeArr.length; i++) {
				params.put("minType", minTypeArr[i]);
				List<Kline> klineList = klineService.findListByMarketId(params);
				redisUtil.setList(RedisKeyEnum.MARKET_KLINE_LIST.getKey() + market.getId() + "_" + minTypeArr[i],
						klineList, -1);
			}
			List<Kline> klineList = klineService.findListByMarketIdGtOrLt(market.getId());
			Map<Integer, List<Kline>> resultMap = MapUtil.sendKlineInfo(klineList);
			for (Map.Entry<Integer, List<Kline>> entry : resultMap.entrySet()) {
				System.out.println(RedisKeyEnum.MARKET_KLINE_LIST.getKey()+market.getId()+"_"+entry.getKey()+"======"+entry.getValue());
				redisUtil.setList(RedisKeyEnum.MARKET_KLINE_LIST.getKey()+market.getId()+"_"+entry.getKey(), entry.getValue(),-1);
			}
		}

	}
}
