package com.oax.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.oax.Constant;
import com.oax.common.ResultResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.oax.common.Params;
import com.oax.common.RedisUtil;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.front.MarketInfo;
import com.oax.entity.front.MarketOrders;
import com.oax.entity.front.Member;
import com.oax.entity.front.Orders;
import com.oax.entity.front.TradeInfo;
import com.oax.entity.front.TradeRecord;
import com.oax.entity.front.UserCoinInfo;
import com.oax.exception.VoException;
import com.oax.mapper.front.MarketMapper;
import com.oax.mapper.front.OrdersMapper;
import com.oax.mapper.front.UserCoinMapper;
import com.oax.service.ApiService;
import com.oax.service.OrdersService;
import com.oax.service.UserService;
import com.oax.vo.CancelOrderApiVO;
import com.oax.vo.CancelOrderVO;
import com.oax.vo.OrdersApiVO;
import com.oax.vo.OrdersVO;


@Service
public class ApiServiceImpl implements ApiService {
	@Autowired
	private MarketMapper marketMapper;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrdersService ordersService;
	
	@Autowired
	private UserCoinMapper userCoinMapper;
	
	@Autowired
	private OrdersMapper ordersMapper;

	@Autowired
	private RedisUtil redisUtil;

	@Override
	@DataSource(DataSourceType.SLAVE)
	public Map<String, Object> getAllCoin(String market) {
		Map<String, Object> map=new HashMap<>();

		//查询市场交易对
		String key = RedisKeyEnum.MARKET_LIST.getKey();
		List<MarketInfo> list = redisUtil.getList(key, MarketInfo.class);
		if(list!=null&&list.size()>0){
				list.stream()
					.forEach(marketInfo -> {
						Map<String, Object> marketMap=new HashMap<>();
						Map<String,Object> orderMap= (Map<String,Object>)redisUtil.getObject(RedisKeyEnum.MARKET_ORDERS_MAP.getKey() + marketInfo.getMarketId(), Map.class);
						List<MarketOrders> buyList = JSON.parseArray(orderMap.get("buyList").toString(), MarketOrders.class);
						List<MarketOrders> sellList = JSON.parseArray(orderMap.get("sellList").toString(), MarketOrders.class);
						if(buyList!=null&&buyList.size()>0){
							marketMap.put("buy",buyList.get(0).getPrice());
						}
						if(sellList!=null&&sellList.size()>0){
							marketMap.put("sell",sellList.get(sellList.size()-1).getPrice());
						}
						marketMap.put("id",marketInfo.getMarketId());
						marketMap.put("low",marketInfo.getMinPrice());
						marketMap.put("high",marketInfo.getMaxPrice());
						marketMap.put("last",marketInfo.getLastTradePrice());
						marketMap.put("volume",marketInfo.getTradeQty());
						marketMap.put("turnover",marketInfo.getTotalAmount());
						marketMap.put("changeRate",marketInfo.getIncRate());
						if(StringUtils.isNotBlank(market)){
							if((marketInfo.getCoinName()+"/"+marketInfo.getMarketCoinName()).equalsIgnoreCase(market)){
								map.put(marketInfo.getCoinName()+"/"+marketInfo.getMarketCoinName(),marketMap);
							}
						}else{
							map.put(marketInfo.getCoinName()+"/"+marketInfo.getMarketCoinName(),marketMap);
						}

				});
		}
		return map;
	}

	@Override
	@DataSource(DataSourceType.SLAVE)
	public List<TradeRecord> getUserTrades(String market, String apiKey) throws VoException {
		  Member user = userService.selectByApiKey(apiKey);  
		  if(user==null) {
			  throw new VoException("apikey错误!");
		  }		  
		  List<TradeRecord> tradeList=marketMapper.getUserTrades(market,user.getId());
		return tradeList;
	}

	@Override
	@DataSource(DataSourceType.SLAVE)
	public List<TradeRecord> getMarketTrades(String market) {
		List<TradeRecord> tradeList=new ArrayList<>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String key1 = RedisKeyEnum.MARKET_LIST.getKey();
		String  key2 = "";
		List<MarketInfo> list1 = redisUtil.getList(key1, MarketInfo.class);
		Optional<MarketInfo> omarketInfo =list1.stream()
			 .filter(marketInfo -> (marketInfo.getCoinName()+"/"+marketInfo.getMarketCoinName()).equalsIgnoreCase(market))
			 .findFirst();

		if(omarketInfo.isPresent()){
			key2=RedisKeyEnum.MARKET_TRADE_LIST.getKey()+omarketInfo.get().getMarketId();
		}

		List<TradeInfo> list = redisUtil.getList(key2,TradeInfo.class);
		if(list!=null&&list.size()>0){
			list.stream().forEach(tradeInfo -> {
				TradeRecord tradeRecord=new TradeRecord();
				tradeRecord.setId(tradeInfo.getId());
				tradeRecord.setPrice(tradeInfo.getPrice());
				tradeRecord.setQty(tradeInfo.getQty());
				tradeRecord.setType(tradeInfo.getType()==1?"buy":"sell");
				tradeRecord.setAmount(tradeInfo.getQty().multiply(tradeInfo.getPrice()).setScale(8,BigDecimal.ROUND_HALF_UP));
				tradeRecord.setCreateTime(tradeInfo.getCreateTime());
				try {
					tradeRecord.setCreateTimeMs(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(tradeRecord.getCreateTime()).getTime());
				}catch (Exception e) {
					e.printStackTrace();
				}

				tradeList.add(tradeRecord);
			});
		}

		return tradeList;
	}

	@Override
	@DataSource(DataSourceType.SLAVE)
	public Map<String, Object> getDepth(String market)throws VoException {
		Integer marketId=marketMapper.getMarketIdByName(market);
		  if(marketId==null) {
			  throw new VoException("该市场暂不能查询!");
		  }
		Map<String, Object> map=ordersService.getMarketOrdersList(marketId);
		return map;
	}

	@Override
	@DataSource(DataSourceType.SLAVE)
	public Map<String, Object> getBanlance(String apiKey)throws VoException {
		  Member user = userService.selectByApiKey(apiKey);  
		  if(user==null) {
			  throw new VoException("apikey错误!");
		  }		  
		  List<UserCoinInfo> userCoinList= userCoinMapper.selectByUserId(user.getId(), null);
		  Map<String, BigDecimal> exchangePrice= userCoinMapper.selectEthPrice(user.getId(), null);
		  
		  BigDecimal ethPrice=exchangePrice.get("ethPrice");			
			//获取eth/btc市场的最新价格
			BigDecimal ethBtcPrice=userCoinMapper.getLastPriceEthBtc();
			if(ethBtcPrice==null) {
				ethBtcPrice=new BigDecimal("0");
			}
			
		  exchangePrice.put("btcPrice", ethBtcPrice.multiply(ethPrice));
		  Map<String, Object>  resultMap=new HashMap<>();
		  
		  resultMap.put("banlance", userCoinList);
		  resultMap.put("exchangePrice", exchangePrice);
		return resultMap;
	}

	@Override
	@DataSource(DataSourceType.SLAVE)
	public List<Map<String,Object>> queryOrder(String apiKey, String market, Integer status) throws VoException{
		  Member user = userService.selectByApiKey(apiKey);  
		  if(user==null) {
			  throw new VoException("apikey错误!");
		  }		  
		  
		  Integer marketId=marketMapper.getMarketIdByName(market);
		  if(marketId==null) {
			  throw new VoException("该市场暂不能查询!");
		  }
		  Params params = new Params();
		  params.put("userId",user.getId());
		  params.put("marketId",marketId);
		  params.put("status",status);
		  
		  List<Map<String,Object>> orderList=ordersMapper.getOrdersByUserIdAndStatus(params);
		return orderList;
	}

	@Override
	public Integer save(OrdersApiVO vo) throws VoException {
		  Member user = userService.selectByApiKey(vo.getApiKey());  
		  if(user==null) {
			  throw new VoException("apikey错误!");
		  }		
		  
		  Integer marketId=marketMapper.getMarketIdByName(vo.getMarket());
		  if(marketId==null) {
			  throw new VoException("该市场暂不能下单!");
		  }

		 if (vo.getPrice().compareTo(BigDecimal.ZERO)<=0) {
			 throw new VoException("价格必须大于0!");
		 }
		 if (vo.getQty().compareTo(BigDecimal.ZERO)<=0) {
			 throw new VoException("数量必须大于0!");
		 }

		  OrdersVO orders=new OrdersVO();
		  orders.setUserId(user.getId());
		  orders.setType(vo.getType());
		  orders.setMachineType(1);
		  orders.setMarketId(marketId);
		  orders.setPrice(vo.getPrice().toString());
		  orders.setQty(vo.getQty().toString());

		ResultResponse result = ordersService.checkUserCoin(orders,"cn");
		  ordersService.insertUserCoin(orders);
		boolean checkFlag = result.isSuccess();
		Integer version = Integer.valueOf(result.getCode());
		  Integer orderId = null;
		  if (checkFlag) {
			  orderId = ordersService.save(orders,"cn",version);
		 }
		 return orderId;
	}

	@Override
	public boolean cancelOrder(CancelOrderApiVO vo)throws VoException {
		 Member user = userService.selectByApiKey(vo.getApiKey());  
		 Orders orders = ordersMapper.getOrderById(vo.getId());
		  if(user==null) {
			  throw new VoException("apikey错误!");
		  }		
		  if(user.getLockStatus()==1) {
			  throw new VoException("账号被锁定!");
		  }	
		  if(orders==null) {
			  throw new VoException("该订单不存在!");
		  }	
		  if(user.getId().intValue()!=orders.getUserId().intValue()) {
			  throw new VoException("您无权撤销该订单!");
		  }		
		  
		  CancelOrderVO cancel=new CancelOrderVO();
		  cancel.setId(vo.getId());
		  cancel.setUserId(user.getId());
		  
		boolean b=ordersService.cancelOrder(cancel);
		return b;
	}
}
