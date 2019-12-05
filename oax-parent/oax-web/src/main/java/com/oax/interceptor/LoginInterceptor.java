package com.oax.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oax.context.HttpContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.oax.Constant;
import com.oax.common.AccessTokenManager;
import com.oax.common.DeviceUtil;
import com.oax.common.ResultResponse;
import com.oax.service.I18nMessageService;

public class LoginInterceptor implements HandlerInterceptor {
	
	@Autowired
	private I18nMessageService i18nMessageService;
	@Autowired
    private AccessTokenManager tokenManager;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {	
		boolean flag=false;
		String userId = request.getHeader(Constant.api_header_userId);
		String accessToken = request.getHeader(Constant.api_header_accessToken);
		String lang = request.getHeader(Constant.api_header_lang);

		if(userId!=null&&!"".equals(userId)&&accessToken!=null&&!"".equals(accessToken)) {
			Integer source=DeviceUtil.getPlatform(request);
			flag=tokenManager.check(Integer.parseInt(userId),accessToken,source);
			//flag=tokenManager.checkTest(Integer.parseInt(userId),accessToken);
			//登录成功，保存信息
			HttpContext.saveUserId(Integer.valueOf(userId));
			HttpContext.saveLang(lang);
		}
		
		if (!flag) {
			//登录失败，清除数据
			HttpContext.clear();

			String msg = i18nMessageService.getMsg(10000, lang);
			
			ResultResponse result = new ResultResponse();
			result.setCode("-1");
			result.setSuccess(false);
			result.setMsg(msg);
			
			String responseConetnt = JSONObject.toJSONString(result);

			response.setContentType("application/json; charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.print(responseConetnt);
			writer.close();
			response.flushBuffer();
		}

		return flag;
        
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		

	}

}
