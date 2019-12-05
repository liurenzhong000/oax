package com.oax.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.oax.common.*;
import com.oax.common.constant.RedisKeyConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;


public class AccessLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            if (!method.isAnnotationPresent(AccessLimit.class)) {
                return true;
            }
            AccessLimit accessLimit = method.getAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;
            }
            int limit = accessLimit.limit();
            int sec = accessLimit.sec();
            String ip = SysUtil.getRemoteIp(request);
            if (StringUtils.isNotBlank(ip) && StringUtils.contains(ip, ",")) {//加了防护好像ip会变成115.199.127.106, 185.254.242.41
                ip = StringUtils.substringBefore(ip, ",");
            }

            if (StringUtils.isBlank(ip)) {//防止ip为空时出现异常
                return true;
            }
            String key = RedisKeyConstant.ASSESS_LIMIT_KEY + ip + request.getRequestURI();
            String maxLimit =redisUtil.getString(key);
            if (maxLimit == null) {
                //set时一定要加过期时间
                redisUtil.setString(key,"1",sec);
            } else if (Integer.parseInt(maxLimit) < limit) {
                Integer value=Integer.parseInt(maxLimit) + 1;
                redisUtil.setString(key,value.toString(),sec);
            } else {
                ResultResponse result = new ResultResponse();
                result.setCode("0");
                result.setSuccess(false);
                result.setMsg("请求太频繁，请稍后重试");
                String responseConetnt = JSONObject.toJSONString(result);

                response.setContentType("application/json; charset=utf-8");
                PrintWriter writer = response.getWriter();
                writer.print(responseConetnt);
                writer.close();
                response.flushBuffer();
                return false;
            }
        }
        return true;
    }

   

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
