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
import com.oax.service.ArticleService;
import com.oax.service.I18nMessageService;
import com.oax.vo.ArticleDetailVO;
import com.oax.vo.ArticleTitleVO;

@RestController
@RequestMapping("/noticeCenter")
public class NoticeCenterController {
	@Autowired
	private ArticleService noticeCenterService;
	
	@Autowired
	private I18nMessageService I18nMessageService;
	
	
	/**
	 * 公告中心列表
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/list")
	public ResultResponse list(HttpServletRequest request) throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);
		
		boolean success = true;
		Object data = null;
		try {
			data=noticeCenterService.noticeList(lang,1);
		} catch (Exception e) {
			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
		}
		return new ResultResponse(success, data);
	}
	
	
	
	/**
	 * 公告中心列表App
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/listApp")
	public ResultResponse listApp(HttpServletRequest request) throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);
		
		boolean success = true;
		Object data = null;
		try {
			data=noticeCenterService.list(lang,2);
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
			data=noticeCenterService.titleList(vo);
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
			data=noticeCenterService.articleDetail(vo);
		} catch (Exception e) {
			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
		}
		return new ResultResponse(success, data);
	}
}
