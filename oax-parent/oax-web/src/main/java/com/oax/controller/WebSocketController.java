package com.oax.controller;



import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oax.common.constant.RedisKeyConstant;
import org.apache.commons.lang3.RandomUtils;
import org.hibernate.internal.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.oax.common.RedisUtil;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.entity.front.Market;
import com.oax.entity.front.MarketInfo;
import com.oax.entity.front.TradeInfo;
import com.oax.entity.front.TradeMarket;
import com.oax.service.MarketService;
import com.oax.service.TradeService;
import com.oax.service.UserCoinService;

@Controller
public class WebSocketController {
	@Autowired
    private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	private TradeService tradeService;
	@Autowired
	private MarketService marketService;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private UserCoinService userCoinService;
    
    @Scheduled(fixedRate = 1000 * 3)
    @Async
    public void notice() throws Exception {
    	
    	List<MarketInfo> marketInfoList = redisUtil.getList(RedisKeyEnum.MARKET_LIST.getKey(), MarketInfo.class);
    	//socket.1 推送所有市场基础信息
    	messagingTemplate.convertAndSend("/topic/marketCategory/all", marketInfoList);
    	List<Market> marketList = marketService.selectAll(); 	
    	 for (Market market : marketList) {		 
    		 int marketId=market.getId();	 
    		 // socket.2 推送 市场 买入卖出订单   市场实时交易记录
			 @SuppressWarnings("unchecked")
			 Map<String, Object> ordersMap = (Map<String, Object>)redisUtil.getObject(RedisKeyEnum.MARKET_ORDERS_MAP.getKey()+market.getId(), Map.class);
			 if (ordersMap == null){
				 ordersMap = new HashMap<>();
			 }
    		 // socket.3单个交易对 成交订单数据量推送
    		 List<TradeInfo> tradeList = redisUtil.getList(RedisKeyEnum.MARKET_TRADE_LIST.getKey()+market.getId(), TradeInfo.class);
			 if(tradeList!=null&&tradeList.size()>0){
				 tradeList.stream().forEach(tradeInfo -> {
					 String createTime=tradeInfo.getCreateTime();
					 tradeInfo.setCreateTime(createTime.substring(createTime.length()-8));
				 });
			 }
    		 ordersMap.put("tradeList", tradeList);
    		 messagingTemplate.convertAndSend("/topic/tradeListAndMarketOrders/"+marketId,ordersMap);     	  		    		 		 
		}
    	 
    }
    
    
    @MessageMapping("/checkTrade/{marketId}/{userId}")//发送地址
    @SendTo("/topic/tradeList/{marketId}/{userId}")//订阅地址
    public String getTradeList(@DestinationVariable int userId,@DestinationVariable int marketId) {
    	// 交易页面用户成交单 ws-person-4.html	
    	Map<String, Object> userData = userCoinService.getUserCoinMap(marketId, userId);
    	List<TradeInfo> marketTradeList = tradeService.getTradeList(marketId, userId);
    	userData.put("marketTradeList", marketTradeList);
        return JSON.toJSONString(userData);
    }
    
    @MessageMapping("/checkMarket/{coinId}")//发送地址
    @SendTo("/topic/marketList/{coinId}")//订阅地址
    public String getMarketList(@DestinationVariable int coinId) {
    	List<TradeMarket> list=userCoinService.selectTradeByCoinId(coinId);
        return JSON.toJSONString(list);
    }

    @Scheduled(fixedRate = 1000 * 2)
    @Async
    public void noticeDiceOutput() throws Exception {
		String betQtyStr = redisUtil.getString(RedisKeyConstant.DICE_BHB_BET_QTY_KEY);
		if (StringHelper.isEmpty(betQtyStr)) {
			betQtyStr = "0";
		}
		String chargesStr = redisUtil.getString(RedisKeyConstant.DICE_BHB_CHARGES_KEY);
		if (StringHelper.isEmpty(chargesStr)) {
			chargesStr = "0";
		}
		String bcbMiningQtyStr = redisUtil.getString(RedisKeyConstant.DICE_BCB_MINING_KEY);
		if (StringHelper.isEmpty(bcbMiningQtyStr)) {
			bcbMiningQtyStr = "0";
		}
		BigDecimal bcbMiningQty = new BigDecimal(bcbMiningQtyStr);
		BigDecimal allBetQty = new BigDecimal(betQtyStr);
		BigDecimal allCharges = new BigDecimal(chargesStr);

        Map<String, Object> map = new HashMap<>();
        map.put("bhbBetQty", allBetQty);
        map.put("bcbOutputQty", bcbMiningQty);
        map.put("charges", allCharges);
        messagingTemplate.convertAndSend("/topic/dice/bhb", JSON.toJSONString(map));
    }

	@Scheduled(fixedRate = 1000 * 2)
	@Async
	public void testToUser() throws Exception {
		messagingTemplate.convertAndSendToUser("219683","/topic/test", RandomUtils.nextInt());
	}
    
}