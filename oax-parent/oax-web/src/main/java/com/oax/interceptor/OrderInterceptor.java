package com.oax.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.oax.Constant;
import com.oax.common.ResultResponse;
import com.oax.mapper.front.OrdersMapper;
import com.oax.service.I18nMessageService;

/** 
* @ClassName:：CheckUserInterceptor 
* @Description：检查用户是否为锁定状态   下订单  撤销订单
* @author ：xiangwh  
* @date ：2018年6月26日 下午4:00:10 
*  
*/
public class OrderInterceptor implements HandlerInterceptor {
	@Autowired
	private I18nMessageService i18nMessageService;
	@Autowired
	private  OrdersMapper ordersMapper;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String lang = request.getHeader(Constant.api_header_lang);
		//必传userId
		Integer userId = Integer.valueOf(request.getHeader(Constant.api_header_userId));
		//verifyUserIsLock
		Integer verifyUserIsLock = ordersMapper.verifyUserIsLock(userId);		
		if (verifyUserIsLock==null||verifyUserIsLock==0) {
			String msg = i18nMessageService.getMsg(10021, lang);
			ResultResponse result = new ResultResponse("-1", false, msg);
			response.setContentType("application/json; charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.print(JSON.toJSONString(result));
			writer.close();
			response.flushBuffer();
			return false;
		}
		return true;
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
