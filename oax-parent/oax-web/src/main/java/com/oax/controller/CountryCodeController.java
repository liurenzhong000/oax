package com.oax.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oax.Constant;
import com.oax.common.ResultResponse;
import com.oax.entity.front.CountryCode;
import com.oax.service.CountryCodeService;
import com.oax.service.I18nMessageService;

@RestController
@RequestMapping("/countryCode")
public class CountryCodeController {
	@Autowired
	private  CountryCodeService countryCodeService;
	@Autowired
	private I18nMessageService I18nMessageService;
	

	/**
	 * 国家地区列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/list")
	public ResultResponse sendSms(HttpServletRequest request) {
		String lang = request.getHeader(Constant.api_header_lang);
		boolean success = true;
		Object data = null;
		try {
			List<CountryCode> list=countryCodeService.list();
			if(list!=null&&list.size()>0) {
				data=list;
			}
		} catch (Exception e) {
			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
		}

		return new ResultResponse(success, data);
	}
}
