package com.oax.controller;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oax.Constant;
import com.oax.common.ResultResponse;
import com.oax.exception.VoException;
import com.oax.exception.VoVailder;
import com.oax.service.HelpCenterService;
import com.oax.service.I18nMessageService;
import com.oax.vo.ArticleDetailVO;
import com.oax.vo.ArticleTitleVO;

@RestController
@RequestMapping("/helpCenter")
public class HelpCenterController {
	@Autowired
	private HelpCenterService helpCenterService;
	
	@Autowired
	private I18nMessageService I18nMessageService;
	
	
	/**
	 * 帮助中心列表
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/list")
	public ResultResponse list(HttpServletRequest request) throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);
		
		boolean success = true;
		Object data = null;
		try {
			data=helpCenterService.list(lang);
		} catch (Exception e) {
			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
		}
		return new ResultResponse(success, data);
	}
	
	/**
	 * 查看更多
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/readMore")
	public ResultResponse readMore(@RequestBody @Valid ArticleTitleVO vo, BindingResult result,HttpServletRequest request) throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);
		VoVailder.valid(result);
		
		boolean success = true;
		Object data = null;
		try {
			vo.setLang(lang);
			data=helpCenterService.titleList(vo);
		} catch (Exception e) {
			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
		}
		return new ResultResponse(success, data);
	}
	
	/**
	 * 查看详情
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/readDetail")
	public ResultResponse readDetail(@RequestBody @Valid ArticleDetailVO vo, BindingResult result,HttpServletRequest request) throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);
		VoVailder.valid(result);
		
		boolean success = true;
		Object data = null;
		try {
			vo.setLang(lang);
			data=helpCenterService.articleDetail(vo);
		} catch (Exception e) {
			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
		}
		return new ResultResponse(success, data);
	}
}
