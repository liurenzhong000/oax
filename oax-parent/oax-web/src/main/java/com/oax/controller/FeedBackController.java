package com.oax.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oax.Constant;
import com.oax.common.ResultResponse;
import com.oax.entity.front.ShareBonusInfo;
import com.oax.exception.VoException;
import com.oax.service.FeedBackService;
import com.oax.service.ShareBonusService;

@RestController
@RequestMapping("/feedBack")
public class FeedBackController {
	@Autowired
	private com.oax.service.I18nMessageService I18nMessageService;

	@Autowired
	private FeedBackService feedBackService;
	@Autowired
	private ShareBonusService shareBonusService;


	/**
	 * 总览
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/overview")
	public ResultResponse queryOverview(HttpServletRequest request){
		Integer userId = null;
		if (StringUtils.isNotEmpty(request.getHeader("userId"))){
			userId = Integer.parseInt(request.getHeader("userId"));
		}
		String lang = request.getHeader(Constant.api_header_lang);
		try {
			Map<String,Object> map=new HashMap<>();
			//总览
			Map<String, Object> overview=feedBackService.queryOverview();
			//回馈累积
			Map<String, Object> feedBackSum=feedBackService.queryFeedBackSum();
			List<ShareBonusInfo> shareBonusInfoList = shareBonusService.getShareBonusInfoList();
			if (userId!=null){
				Map<String, Object> myIncome = feedBackService.queryMyIncome(userId);
				map.put("myIncome", myIncome);
			}else{
				map.put("myIncome", null);
			}
			map.put("overview",overview);
			map.put("feedBackSum",feedBackSum);
			map.put("shareBonusList", shareBonusInfoList);
			return new ResultResponse(true,map);
		} catch (Exception e) {
			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
		}
	}

	/**
	 * 我的收益
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/user/myIncome")
	public ResultResponse myIncome(HttpServletRequest request){
		String lang = request.getHeader(Constant.api_header_lang);
		String userId = request.getHeader(Constant.api_header_userId);
		try {
			//我的收益
			Map<String, Object> myIncome=feedBackService.queryMyIncome(Integer.parseInt(userId));
			return new ResultResponse(true,myIncome);
		} catch (Exception e) {
			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
		}
	}

	/**
	 * 今日回馈详情
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/todayFeedBackdetail")
	public ResultResponse todayFeedBackdetail(HttpServletRequest request){
		String lang = request.getHeader(Constant.api_header_lang);
		try {
			Map<String,Object> map=new HashMap<>();
			Map<String, Object> feedBackSum=feedBackService.queryFeedBackSum();
			List<Map<String, Object>> feedBackDetail=feedBackService.queryTodayFeedBackdetail();

			map.put("feedBackSum",feedBackSum);
			map.put("feedBackDetailList",feedBackDetail);
			return new ResultResponse(true,map);
		} catch (Exception e) {
			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
		}
	}

	/**
	 * 总览App
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/app/overview")
	public ResultResponse queryOverviewApp(HttpServletRequest request){
		String lang = request.getHeader(Constant.api_header_lang);
		try {
			//总览
			Map<String, Object> overview=feedBackService.queryOverview();
			return new ResultResponse(true,overview);
		} catch (Exception e) {
			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
		}
	}

	/**
	 * 回馈累积
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/app/feedBackSum")
	public ResultResponse feedBackSum(HttpServletRequest request){
		String lang = request.getHeader(Constant.api_header_lang);
		try {
			//回馈累积
			Map<String, Object> feedBackSum=feedBackService.queryFeedBackSum();
			return new ResultResponse(true,feedBackSum);
		} catch (Exception e) {
			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
		}
	}
}
