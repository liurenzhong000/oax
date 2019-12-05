package com.oax.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.oax.Constant;
import com.oax.common.ResultResponse;
import com.oax.exception.VoException;
import com.oax.service.I18nMessageService;
import com.oax.util.WyyVerifierUtil;

@RestController
@RequestMapping("/thirdApi")
public class ThirdApiController {
	//验证码id
	@Value("${wyy.verify.captchaId}")
	private  String captchaId;
	
	//密钥id
	@Value("${wyy.verify.secretId}")
	private String secretId;
	
	//密钥key
	@Value("${wyy.verify.secretKey}")
	private String secretKey;
	
	//二次验证接口请求地址
	@Value("${wyy.verify.url}")
	private String verifyUrl;
	
	//版本 取固定值v2
	private static final String VERSION = "v2";
	
	@Autowired
	private I18nMessageService I18nMessageService;
	
	
    /**
     * 网易云滑块验证码二次校验
     * @throws VoException
     */
    @PostMapping("/wyy/secondVerify")
    public ResultResponse secondVerify(@RequestBody String jsonString,HttpServletRequest request) throws VoException{
    	String lang = request.getHeader(Constant.api_header_lang);
    	String validate=JSON.parseObject(jsonString).getString("validate");
    	
    	if(StringUtils.isBlank(jsonString)) {
			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
		}
    	WyyVerifierUtil wyy=new WyyVerifierUtil(captchaId,secretId,secretKey,verifyUrl,VERSION);
    	
    	boolean b=wyy.verify(validate,null);
    	if(b) {
    		return new ResultResponse(true, "success");
    	}else {
    		return new ResultResponse(false, "fail");
    	}
       
    }
    
}
