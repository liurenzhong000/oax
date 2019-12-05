package com.oax.interceptor;

import com.oax.common.AssertHelper;
import com.oax.common.SignUtil;
import com.oax.exception.VoException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.TreeMap;

/**
 * @Auther: hyp
 * @Date: 2019/1/23 15:19
 * @Description:
 */
public class ApiThirdInterceptor implements HandlerInterceptor {

    public static String AUTH_KEY = "6&3h&%$he*&@mwu";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String sign = request.getParameter("sign");
        String timestampStr = request.getParameter("timestamp");
        if (StringUtils.isBlank(sign) || StringUtils.isBlank(timestampStr)) {
            throw new VoException("参数错误!");
        }
        Long timestamp = Long.parseLong(timestampStr);
        AssertHelper.isTrue(System.currentTimeMillis() - timestamp <= 1000*120, "参数错误!");//时间戳验证有效性为2分钟

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
        if (!SignUtil.encryptForApiThird(AUTH_KEY, params).equals(sign)) {
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
