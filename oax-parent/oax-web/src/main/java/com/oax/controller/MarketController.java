package com.oax.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oax.common.ResultResponse;
import com.oax.entity.front.MarketCategoryResult;
import com.oax.service.MarketService;

/** 
* @ClassName:：MarketController 
* @Description： 交易对入口
* @author ：xiangwh  
* @date ：2018年6月14日 下午4:49:31 
*  
*/
@RestController
@RequestMapping("market")
public class MarketController {
	@Autowired
	private MarketService marketService;
	
	@RequestMapping("/list")
	public ResultResponse getMarketList() {
		List<Map<String, Object>> list = marketService.getMarketList();
		return new ResultResponse(true, list);
	}
	
	@RequestMapping("/marketCategoryList")
	public ResultResponse marketCategoryList() {
		List<MarketCategoryResult> marketCategoryList = marketService.getMarketCategoryInfo();
		return new ResultResponse(true, marketCategoryList);
	}
}
