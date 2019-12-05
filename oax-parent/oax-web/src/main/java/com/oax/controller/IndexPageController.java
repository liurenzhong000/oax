package com.oax.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oax.common.DeviceUtil;
import com.oax.common.ResultResponse;
import com.oax.entity.front.ArticleTypeDetail;
import com.oax.entity.front.BannerInfo;
import com.oax.entity.front.MarketInfo;
import com.oax.outparam.CategoryMarket;
import com.oax.service.ArticleService;
import com.oax.service.BannerService;
import com.oax.service.TradeCoinService;
import com.oax.service.UserMaketService;

/** 
* @ClassName:：IndexPageController 
* @Description： 首页接口
* @author ：xiangwh  
* @date ：2018年6月22日 下午5:07:44 
*  
*/
@RestController
@RequestMapping("/")
public class IndexPageController {
	@Autowired
	private BannerService bannerService;
	
	@Autowired
	private TradeCoinService tradeCoinService;
	
	@Autowired
	private  UserMaketService userMaketService;
	
	@Autowired 
	private  ArticleService articleService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/indexPage")
	public ResultResponse indexPageInfo(@RequestHeader String lang,HttpServletRequest request) {
		Integer userId = null;
		if (StringUtils.isNotBlank(request.getHeader("userId"))) {
			userId = Integer.valueOf(request.getHeader("userId"));
		}
		
		Map<String,Object> resultMap=new HashMap<>();
		int device=DeviceUtil.getPlatform(request);	
		 List<BannerInfo> bannerList = bannerService.findList(device, lang);
		 
		 
		 List<ArticleTypeDetail> articleList=(List<ArticleTypeDetail>)articleService.noticeList(lang, 2).get("list");
		 List<Map<String, Object>> recommendMarketList = tradeCoinService.findIndexPageMarket(); 
		 List<CategoryMarket> allMaketList = tradeCoinService.findList(userId);
		 List<MarketInfo> userMaketList = null;
		 if(userId!=null) {
			 userMaketList= userMaketService.findListByUser(userId);
		 }
		 
		 resultMap.put("bannerList", bannerList);
		 resultMap.put("articleList", articleList);
		 resultMap.put("recommendMarketList", recommendMarketList);
		 resultMap.put("allMaketList", allMaketList);
		 resultMap.put("userMaketList", userMaketList);
		 
		return new ResultResponse(true,resultMap);
	}
}
