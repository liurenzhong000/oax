package com.oax.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.common.RedisUtil;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.front.MarketInfo;
import com.oax.mapper.front.BenchMarkShareBonusMapper;
import com.oax.mapper.front.InvitingFeedbackMapper;
import com.oax.mapper.front.TradeFeedBackLogMapper;
import com.oax.mapper.front.TradeSnapshotMapper;
import com.oax.service.FeedBackService;


@Service
public class FeedBackServiceImpl implements FeedBackService {
	@Autowired
	private TradeSnapshotMapper tradeSnapshotMapper;

	@Autowired
	private TradeFeedBackLogMapper tradeFeedBackLogMapper;

	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private InvitingFeedbackMapper invitingFeedbackMapper;

	@Autowired
	private BenchMarkShareBonusMapper benchMarkShareBonusMapper;

	@Override
	@DataSource(DataSourceType.SLAVE)
	public Map<String, Object> queryOverview() {
		Map<String,Object> map=new HashMap<>();
		//x币总流通量
		BigDecimal circulationTotal=tradeSnapshotMapper.getCirculationTotal();
		BigDecimal total=new BigDecimal("1000000000");

		//获取x币在ETH中的最新价格
		BigDecimal lastTradePrice=new BigDecimal("0");
		String key = RedisKeyEnum.MARKET_LIST.getKey();
		List<MarketInfo> marketInfoList = redisUtil.getList(key, MarketInfo.class);
		Optional<MarketInfo> marketInfoOptional = marketInfoList.stream()
				.filter(market -> "X".equalsIgnoreCase(market.getCoinName()) && "ETH".equalsIgnoreCase(market.getMarketCoinName()))
				.findFirst();

		if (marketInfoOptional.isPresent()) {
			MarketInfo marketInfo = marketInfoOptional.get();
			lastTradePrice= marketInfo.getLastTradePrice();
		}
		//x币总市值
		BigDecimal totalMarketValue=total.multiply(lastTradePrice);
		//x币流通市值
		BigDecimal circulationMarketValue=circulationTotal.multiply(lastTradePrice);

		map.put("circulationTotal",circulationTotal.setScale(2, BigDecimal.ROUND_HALF_DOWN).toString());
		map.put("total",total.setScale(2, BigDecimal.ROUND_HALF_DOWN).toString());
		map.put("totalMarketValue",totalMarketValue.setScale(2, BigDecimal.ROUND_HALF_DOWN).toString());
		map.put("circulationMarketValue",circulationMarketValue.setScale(2, BigDecimal.ROUND_HALF_DOWN).toString());
		return map;
	}

	@Override
	@DataSource(DataSourceType.SLAVE)
	public Map<String, Object> queryFeedBackSum() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		String beginTime = sdf.format(c.getTime());
		Map<String,Object> map=new HashMap<>();
		//今日待分配总额
		Map<String,BigDecimal> todayFeedBackSum=tradeSnapshotMapper.getFeedBackSum(beginTime);

		//今日交易待分配
		Map<String,BigDecimal> todayTradeFeedBack=new HashMap<>();
		todayFeedBackSum.forEach((key, value)->todayTradeFeedBack.put(key, value.multiply(new BigDecimal("0.8")).setScale(2, BigDecimal.ROUND_HALF_DOWN)));

		//今日补偿待分配
		Map<String,BigDecimal> todayCompensationFeedBack=new HashMap<>();
		todayFeedBackSum.forEach((key, value)->todayCompensationFeedBack.put(key, value.multiply(new BigDecimal("0.2")).setScale(2, BigDecimal.ROUND_HALF_DOWN)));

		map.put("todayFeedBackSum",todayFeedBackSum);
		map.put("todayTradeFeedBack",todayTradeFeedBack);
		map.put("todayCompensationFeedBack",todayCompensationFeedBack);
		return map;
	}

	@Override
	@DataSource(DataSourceType.SLAVE)
	public Map<String, Object> queryMyIncome(Integer userId) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		Calendar c = Calendar.getInstance();
		String beginTime = sdf.format(c.getTime());
		c.add(Calendar.DATE, 1);
		String endTime = sdf.format(c.getTime());
		BigDecimal yesterdayFeedBack=tradeFeedBackLogMapper.getMyFeedBack(userId,beginTime,endTime);
		BigDecimal yesterdayInvitingFeedBack = invitingFeedbackMapper.getMyInvitingFeedback(userId,beginTime,endTime);
		List<Map<String, Object>> yesterdayShareBonus = benchMarkShareBonusMapper.getMyShareBonus(userId, beginTime, endTime);
		Map<String,Object> map=new HashMap<>();
		map.put("yesterdayFeedBack",yesterdayFeedBack.setScale(2, BigDecimal.ROUND_FLOOR));
		map.put("yesterdayInvitingFeedBack", yesterdayInvitingFeedBack);
		map.put("yesterdayShareBonus", yesterdayShareBonus);
		return map;
	}

	@Override
	@DataSource(DataSourceType.SLAVE)
	public List<Map<String, Object>> queryTodayFeedBackdetail() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		Calendar c = Calendar.getInstance();
		String beginTime = sdf.format(c.getTime());

		List<Map<String, Object>> todayFeedBackdetail=tradeSnapshotMapper.getTodayFeedBackdetail(beginTime);
		return todayFeedBackdetail;
	}
}
