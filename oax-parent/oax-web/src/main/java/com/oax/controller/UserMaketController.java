package com.oax.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oax.common.ResultResponse;
import com.oax.entity.front.MarketInfo;
import com.oax.entity.front.UserMaket;
import com.oax.service.I18nMessageService;
import com.oax.service.UserMaketService;
import com.oax.vo.UserMaketVO;

@RestController
@RequestMapping("userMaket")
public class UserMaketController {
	@Autowired
	private UserMaketService userMaketService;
	@Autowired
    private I18nMessageService msgService;
	
	@GetMapping("/save/{marketId}")
	public ResultResponse save(@PathVariable(name = "marketId") int marketId,HttpServletRequest request) {	
		UserMaket userMaket = new UserMaket();
		userMaket.setUserId(request.getIntHeader("userId"));
		userMaket.setMarketId(marketId);
		return userMaketService.save(userMaket,request);
	}
	
	@PostMapping("/getMarketListByUser")
	public ResultResponse getMarketListByUser(@RequestBody UserMaketVO vo,@RequestHeader int userId) {
//		Map<String, Integer> map  = new HashMap<>();
//		map.put("coinId", vo.getCoinId());
//		map.put("userId", userId);
		List<MarketInfo> list = userMaketService.findListByUser(userId);
		return new ResultResponse(true, list);
	}
	
	@RequestMapping("cancel/{marketId}")
	public ResultResponse cancel(@PathVariable(name = "marketId") Integer marketId,@RequestHeader Integer userId,@RequestHeader String lang) {
		String msg = null;
		boolean flag = userMaketService.delete(marketId, userId);
		if (flag) {
			msg =msgService.getMsg(10029, lang);
		}else {
			msg =msgService.getMsg(10100, lang);	
		}
		return new ResultResponse(flag, msg);
		
	}
	
}
