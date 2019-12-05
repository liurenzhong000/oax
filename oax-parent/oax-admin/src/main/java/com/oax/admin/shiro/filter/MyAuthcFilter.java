package com.oax.admin.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import com.alibaba.fastjson.JSON;
import com.oax.common.ResultResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/7
 * Time: 19:38
 * <p>
 * 自定义 shiro登录过滤器
 * -> 支持restful url
 * -> 错误(未登录) 返回json
 */
@Slf4j
public class MyAuthcFilter extends FormAuthenticationFilter {


    /**
     * 重写URL匹配  加入httpMethod支持
     * @param path
     * @param request
     * @return
     */
    @Override
    protected boolean pathsMatch(String path, ServletRequest request) {
        String requestURI = this.getPathWithinApplication(request);
        // path: url==method eg: http://api/menu==GET   需要解析出path中的url和httpMethod
        String[] strings = path.split("==");
        if (strings.length <= 1) {
            // 分割出来只有URL
            return this.pathsMatch(strings[0], requestURI);
        } else {
            // 分割出url+httpMethod,判断httpMethod和request请求的method是否一致,不一致直接false
            String httpMethod = WebUtils.toHttp(request).getMethod().toUpperCase();
            return httpMethod.equals(strings[1].toUpperCase()) && this.pathsMatch(strings[0], requestURI);
        }
    }


    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
//        return super.onAccessDenied(request, response);

        if (this.isLoginRequest(request, response)) {
            if (this.isLoginSubmission(request, response)) {
                if (log.isTraceEnabled()) {
                    log.trace("Login submission detected.  Attempting to execute login.");
                }

                return this.executeLogin(request, response);
            } else {
                if (log.isTraceEnabled()) {
                    log.trace("Login page view.");
                }

                return true;
            }
        } else {
            if (log.isTraceEnabled()) {
                log.trace("Attempting to access a path which requires authentication.  Forwarding to the Authentication url [" + this.getLoginUrl() + "]");
            }
            HttpServletResponse res = (HttpServletResponse) response;
            //设置编码格式
            response.setContentType("json/plain;charset=" + "UTF-8");
            response.setCharacterEncoding("UTF-8");

            ResultResponse resultResponse = new ResultResponse();
            resultResponse.setCode("-1");
            resultResponse.setMsg("没有登录,请登陆");
            resultResponse.setSuccess(false);
            //输出 JSON 字符串
            res.getWriter().print(
                    JSON.toJSON(resultResponse)
            );
            //中止请求，不再传给下一个Filter
            return false;
        }
    }
}
