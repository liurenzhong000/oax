package com.oax.interceptor;

import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.oax.common.SignUtil;
import com.oax.entity.front.Member;
import com.oax.exception.VoException;
import com.oax.service.UserService;

public class ApiInterceptor implements HandlerInterceptor {
	@Autowired
	private UserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {	
		// apiKey 
        String apiKey = request.getParameter("apiKey");  
        // 签名  
        String sign = request.getParameter("sign");  
        
        if (StringUtils.isBlank(apiKey) || StringUtils.isBlank(sign)) {  
        	throw new VoException("参数错误!");
        }  
        
        Member user = userService.selectByApiKey(apiKey);
        if(user==null) {
        	throw new VoException("apikey错误!");
        }
        
        // apiSecret 
        String apiSecret ="XBTC2018"+user.getId();
        
        //签名
        TreeMap<String, String> params = new TreeMap<String, String>(); 
        Enumeration<String> enu = request.getParameterNames();  
        while (enu.hasMoreElements()) {  
            String paramName = enu.nextElement().trim();  
            if (!paramName.equals("sign")) {  
                params.put(paramName, URLDecoder.decode(request.getParameter(paramName), "UTF-8"));  
            }  
        }

       //校验签名
        if (!SignUtil.sign(apiSecret,params).equals(sign)) {
        	throw new VoException("签名错误!");
        }else {
        	return true;
        } 
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
