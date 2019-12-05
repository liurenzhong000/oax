package com.oax.common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RPCRquest {


    private static String sendPostRequest(String url, String param) {
        HttpURLConnection httpURLConnection = null;
        OutputStream out = null; // 写
        InputStream in = null; // 读
        int responseCode = 0; // 远程主机响应的HTTP状态码
        String result = "";
        try {
            URL sendUrl = new URL(url);
            httpURLConnection = (HttpURLConnection) sendUrl.openConnection();
            // post方式请求
            httpURLConnection.setRequestMethod("POST");
            // 设置头部信息
            httpURLConnection.setRequestProperty("headerdata", "ceshiyongde");
            // 一定要设置 Content-Type 要不然服务端接收不到参数
            httpURLConnection.setRequestProperty("Content-Type", "application/Json; charset=UTF-8");
            // 指示应用程序要将数据写入URL连接,其值默认为false（是否传参）
            httpURLConnection.setDoOutput(true);
            // httpURLConnection.setDoInput(true);

            httpURLConnection.setUseCaches(false);
            httpURLConnection.setConnectTimeout(10000); // 10秒连接超时
            httpURLConnection.setReadTimeout(10000); // 10秒读取超时
            // 获取输出流
            out = httpURLConnection.getOutputStream();
            // 输出流里写入POST参数
            out.write(param.getBytes());
            out.flush();
            out.close();
            responseCode = httpURLConnection.getResponseCode();
            BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
            result = br.readLine();
        } catch (Exception e) {
            log.error("请求BTC钱包服务器失败,url:{},参数:{}",url,param);
        }

        return result;

    }


    public static String invoke(String url, String method, Object... params) throws Exception {

        Map<String, Object> map = new HashMap<>();
        map.put("id", "1");
        map.put("jsonrpc", "2.0");
        map.put("method", method);
        map.put("params", params);
        String body = JSON.toJSONString(map);

        String resultJSON = sendPostRequest(url, body);

        JSONObject resJson = JSONObject.parseObject(resultJSON);

        String result;
        try {
            result = resJson.get("result").toString();
        } catch (Exception e) {
            log.error("ETH-RPC-API-ERROR::{}", resultJSON);
            throw new NullPointerException("返回result 为null,resultJSON为:" + resultJSON);
        }
        return result;
    }


}
