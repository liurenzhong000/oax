package com.oax.filter;

import com.oax.Constant;
import com.oax.common.AccessTokenManager;
import com.oax.common.DeviceUtil;
import com.oax.context.HttpContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class BaseFilter implements Filter {

    @Autowired
    private AccessTokenManager tokenManager;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

//        System.out.println(req.getMethod());
//        resp.setHeader("Access-Control-Allow-Origin", "*");
//        resp.setHeader("Access-Control-Allow-Methods", "*");
//        resp.setHeader("Access-Control-Max-Age", "1728000");
//        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, x-requested-with, X-Custom-Header, Authorization");

        boolean flag = false;

        String userId = req.getHeader(Constant.api_header_userId);
        String accessToken = req.getHeader(Constant.api_header_accessToken);
        String lang = req.getHeader(Constant.api_header_lang);
        if(!StringUtils.isBlank(userId) && !StringUtils.isBlank(accessToken)) {
            Integer source = DeviceUtil.getPlatform(req);
            flag = tokenManager.check(Integer.parseInt(userId),accessToken,source);
            //登录成功，保存信息
            HttpContext.saveUserId(Integer.valueOf(userId));
            HttpContext.saveLang(lang);
        }

        if (!flag) {
            //登录失败，清除数据
            HttpContext.clear();
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
