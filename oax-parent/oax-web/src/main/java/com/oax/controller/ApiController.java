package com.oax.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oax.common.ResultResponse;
import com.oax.entity.front.TradeRecord;
import com.oax.exception.VoException;
import com.oax.service.ApiService;
import com.oax.vo.CancelOrderApiVO;
import com.oax.vo.OrdersApiVO;

/**
 * 对外Api
 */
@RestController
@RequestMapping("/api")
public class ApiController {
	@Autowired
	private ApiService apiService;


	/**
	 * 获取所有市场行情
	 * @return
	 * @throws VoException
	 */
    @RequestMapping("/market/getAllMarketInfo")
    public ResultResponse getAllMarketInfo() throws VoException{
    	
    	Map<String, Object> map=apiService.getAllCoin(null);
    	
		return new ResultResponse(true,map);
    }
    
    /**
	 * 获取单个市场行情
	 * @param market
	 * @return
	 * @throws VoException
	 */
    @RequestMapping("/market/getMarketInfo")
    public ResultResponse getMarketInfo(String market) throws VoException{
    	if(StringUtils.isBlank(market)) {
    		return new ResultResponse(false,"市场名称不能为空!");
    	}
    	Map<String, Object> map=apiService.getAllCoin(market);
		return new ResultResponse(true,map);
    }
    
    /**
 	 * 获取用户单个市场交易记录
 	 * @param market,apiKey
 	 * @return
 	 * @throws VoException
 	 */
     @RequestMapping("/user/getTrades")
     public ResultResponse getUserTrades(String market,String apiKey) throws VoException{
     	if(StringUtils.isBlank(market)) {
     		return new ResultResponse(false,"市场名称不能为空!");
     	}
     	List<TradeRecord> tradeList=apiService.getUserTrades(market,apiKey);
 		return new ResultResponse(true,tradeList);
     }
     
     /**
  	 * 获取单个市场交易记录
  	 * @param market
  	 * @return
  	 * @throws VoException
  	 */
      @RequestMapping("/market/getTrades")
      public ResultResponse getMarketTrades(String market) throws VoException{
      	if(StringUtils.isBlank(market)) {
      		return new ResultResponse(false,"市场名称不能为空!");
      	}
      	List<TradeRecord> tradeList=apiService.getMarketTrades(market);
  		return new ResultResponse(true,tradeList);
      }
      
  	/**
	 * 获取单个币种深度
	 * @param market
	 * @return
	 * @throws VoException
	 */
    @RequestMapping("/market/getDepth")
    public ResultResponse getDepth(String market) throws VoException{
    	if(StringUtils.isBlank(market)) {
    		return new ResultResponse(false,"市场名称不能为空!");
    	}
    	
    	Map<String, Object> map=apiService.getDepth(market);
		return new ResultResponse(true,map);
    }
    
  	/**
	 * 获取用户所有余额
	 * @param apiKey
	 * @return
	 * @throws VoException
	 */
    @RequestMapping("/user/getBanlance")
    public ResultResponse getBanlance(String  apiKey) throws VoException{
    	Map<String,Object> map=apiService.getBanlance(apiKey);
		return new ResultResponse(true,map);
    }
    
  	/**
	 * 获取用户订单
	 * @param market,apiKey,status
	 * @return
	 * @throws VoException
	 */
    @RequestMapping("/user/queryOrder")
    public ResultResponse queryOrder(String  apiKey,String market,Integer status) throws VoException{
    	if(StringUtils.isBlank(market)) {
    		return new ResultResponse(false,"市场名称不能为空!");
    	}
		List<Map<String,Object>> orderList=apiService.queryOrder(apiKey,market,status);
		return new ResultResponse(true,orderList);
    }
    
  	/**
	 * 下单
	 * @return
	 * @throws VoException
	 */
    @RequestMapping("/user/addOrder")
    public ResultResponse addOrder(OrdersApiVO vo) throws VoException{
		Integer orderId = apiService.save(vo);
		if(orderId!=null) {
			return new ResultResponse(true,orderId);
		}else {
			return new ResultResponse(false,"下单失败!");
		}
		
    }
    
  	/**
	 * 撤销订单
	 * @return
	 * @throws VoException
	 */
    @RequestMapping("/user/cancelOrder")
    public ResultResponse cancelOrder(CancelOrderApiVO vo) throws VoException{
		boolean flag = apiService.cancelOrder(vo);
		if(flag) {
			return new ResultResponse(true,"撤单成功!");
		}else {
			return new ResultResponse(false,"撤单失败!");
		}
		
    }
}
