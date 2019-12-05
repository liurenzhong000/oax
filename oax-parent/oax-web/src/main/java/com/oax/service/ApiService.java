package com.oax.service;

import java.util.List;
import java.util.Map;

import com.oax.entity.front.TradeRecord;
import com.oax.exception.VoException;
import com.oax.vo.CancelOrderApiVO;
import com.oax.vo.OrdersApiVO;

public interface ApiService {

	Map<String, Object> getAllCoin(String market);

	List<TradeRecord> getUserTrades(String market, String apiKey)throws VoException;

	List<TradeRecord> getMarketTrades(String market);

	Map<String, Object> getDepth(String market) throws VoException;

	Map<String, Object> getBanlance(String apiKey)throws VoException;

	List<Map<String,Object>> queryOrder(String apiKey, String market, Integer status)throws VoException;

	Integer save(OrdersApiVO vo) throws VoException;

	boolean cancelOrder(CancelOrderApiVO vo)throws VoException;

}
