package com.oax.service.api;

import com.oax.common.HttpRequestUtil;
import com.oax.common.SignUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Auther: hyp
 * @Date: 2019/1/23 15:40
 * @Description:
 */
@SpringBootTest
public class ApiThirdTest {

    String AUTH_KEY = "6&3h&%$he*&@mwu";
    @Test
    public void test(){
        TreeMap<String, String> params = new TreeMap<String, String>();
        String timestamp = System.currentTimeMillis() + "";
        String userId = "219698";
        String accessToken = "1721862253b4dbf43ffb604fd2966c6be1cb9efe7e";
        params.put("timestamp", timestamp);
        params.put("userId", userId);
        params.put("accessToken", accessToken);
        String sign = SignUtil.encryptForApiThird(AUTH_KEY, params);
        params.put("sign", sign);
        System.out.println(sign);
        System.out.println(buildRequestParams(params));
        String responseStr = HttpRequestUtil.sendGet("http://localhost:8080/api/third/checkByToken", buildRequestParams(params));
        System.out.println(responseStr);
    }

    @Test
    public void testGetBalance(){
        TreeMap<String, String> params = new TreeMap<String, String>();
        String timestamp = System.currentTimeMillis() + "";
        String userId = "219698";
        params.put("timestamp", timestamp);
        params.put("userId", userId);
        String sign = SignUtil.encryptForApiThird(AUTH_KEY, params);
        params.put("sign", sign);
        String responseStr = HttpRequestUtil.sendGet("http://localhost:8080/api/third/getBalanceByCoinId", buildRequestParams(params));
        System.out.println(responseStr);
    }

    @Test
    public void testChangeBalance(){
        TreeMap<String, String> params = new TreeMap<String, String>();
        String timestamp = System.currentTimeMillis() + "";
        String userId = "219698";
        params.put("timestamp", timestamp);
        params.put("userId", userId);
        params.put("qty", BigDecimal.ONE.toPlainString());//余额变更,如果是减就传负数
        params.put("targetId", "1");
        String sign = SignUtil.encryptForApiThird(AUTH_KEY, params);
        params.put("sign", sign);
        String responseStr = HttpRequestUtil.sendPost("http://localhost:8080/api/third/chargeBalance", buildRequestParams(params));
        System.out.println(responseStr);
    }

    public static String buildRequestParams(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String param : params.keySet()) {
            sb.append(param).append("=").append(params.get(param));
            sb.append("&");
        }
        String requestParams = StringUtils.substringBeforeLast(sb.toString(), "&");
        return requestParams;
    }


}
