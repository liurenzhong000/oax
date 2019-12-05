package com.oax.admin.shiro.filter;

import java.util.Arrays;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

import com.alibaba.fastjson.JSON;
import com.oax.common.ResultResponse;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/8
 * Time: 10:55
 * <p>
 * 自定义 shiro权限过滤器
 * -> 支持restful url格式
 * -> 错误(权限不足)返回json
 */
public class MyPermissionsAuthorizationFilter extends PermissionsAuthorizationFilter {

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

    //在非法用户权限时调用
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
//        return super.onAccessDenied(request, response, mappedValue);

        //获取被要求的权限
        String[] perms = (String[]) mappedValue;

        HttpServletResponse res = (HttpServletResponse) response;
        //设置编码格式
        response.setContentType("json/plain;charset=" + "UTF-8");
        response.setCharacterEncoding("UTF-8");
        //输出 JSON 字符串
        res.getWriter().print(
                JSON.toJSON(new ResultResponse(false, "抱歉没有" + Arrays.toString(perms) + "权限"))
        );
        //中止请求，不再传给下一个Filter
        return false;
    }


}
