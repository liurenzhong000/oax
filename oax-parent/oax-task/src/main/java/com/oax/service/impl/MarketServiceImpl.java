package com.oax.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.entity.front.Market;
import com.oax.entity.front.MarketInfo;
import com.oax.entity.front.MarketOrders;
import com.oax.entity.front.TradeInfo;
import com.oax.mapper.front.MarketMapper;
import com.oax.mapper.front.OrdersMapper;
import com.oax.mapper.front.TradeCoinMapper;
import com.oax.mapper.front.TradeMapper;
import com.oax.service.MarketService;

/** 
* @ClassName:：MarketServiceImpl 
* @Description： 查询所有交易对信息
* @author ：xiangwh  
* @date ：2018年7月3日 下午3:13:58 
*  
*/
@Service
public class MarketServiceImpl implements MarketService{

	@Autowired
	private MarketMapper mapper;
	@Autowired
	private TradeCoinMapper tradeCoinMapper;
	@Autowired
	private OrdersMapper ordersMapper;
	@Autowired
	private TradeMapper tradeMapper;

	@Override
	public List<Market> selectAll() {
		return mapper.selectListAll();
	}

	@Override
	public List<Market> allMarketsOnShelf() {
		List<Market> list = mapper.allMarketsOnShelf();
		return list;
	}

	@Override
	public List<MarketInfo> allMarketInfo() {
		List<MarketInfo> list = tradeCoinMapper.findList();
		return list;
	}

	@Override
	public Map<String, Object> getMarketOrdersMap(Integer marketId) {
		Map<String, Object> result = new HashMap<>();
		//查买入 
		List<MarketOrders> buyList = ordersMapper.findMarketOrdersList(marketId,1);
		//查卖出
		List<MarketOrders> sellList = ordersMapper.findMarketOrdersList(marketId,2);
		Collections.reverse(sellList);
		
		result.put("buyList", buyList);
		result.put("sellList", sellList);		
		return result;
	}

	@Override
	public List<TradeInfo> getMarketTradeList(Integer marketId) {
		List<TradeInfo> list = tradeMapper.getTradeList(marketId,null);
		return list;
	}

    @Override
    public List<Market> selectByType(byte status) {
        return mapper.selectByType(status);
    }

}
