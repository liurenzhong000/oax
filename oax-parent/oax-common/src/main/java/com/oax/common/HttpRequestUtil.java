package com.oax.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpRequestUtil {
    private static HttpClient defaultClient = createHttpClient(50, 30, 3000, 3000, 3000);


    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     * @param param
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }


    /**
     * 实例化HttpClient
     *
     * @param maxTotal
     * @param maxPerRoute
     * @param socketTimeout
     * @param connectTimeout
     * @param connectionRequestTimeout
     * @return
     */
    public static HttpClient createHttpClient(int maxTotal, int maxPerRoute, int socketTimeout, int connectTimeout, int connectionRequestTimeout) {
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout).build();

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(maxTotal);
        cm.setDefaultMaxPerRoute(maxPerRoute);
        cm.setValidateAfterInactivity(200); // 一个连接idle超过200ms,再次被使用之前,需要先做validation
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .setConnectionTimeToLive(30, TimeUnit.SECONDS)
                .setRetryHandler(new StandardHttpRequestRetryHandler(3, true)) // 配置出错重试
                .setDefaultRequestConfig(defaultRequestConfig).build();

        startMonitorThread(cm);

        return httpClient;
    }

    /**
     * 增加定时任务, 每隔一段时间清理连接
     *
     * @param cm
     */
    private static void startMonitorThread(final PoolingHttpClientConnectionManager cm) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        cm.closeExpiredConnections();
                        cm.closeIdleConnections(30, TimeUnit.SECONDS);
                        // log.info("closing expired & idle connections, stat={}", cm.getTotalStats());
                        TimeUnit.SECONDS.sleep(10);
                    } catch (Exception e) {
                        // ignore exceptoin
                    }
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }


    /**
     * 发送post请求
     *
     * @param httpClient
     * @param url        请求地址
     * @param params     请求参数
     * @param encoding   编码
     * @return
     */
    public static String sendPost(HttpClient httpClient, String url, Map<String, String> params, Charset encoding) {
        String resp = "";
        HttpPost httpPost = new HttpPost(url);
        if (params != null && params.size() > 0) {
            List<NameValuePair> formParams = new ArrayList<NameValuePair>();
            Iterator<Map.Entry<String, String>> itr = params.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry<String, String> entry = itr.next();
                formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(formParams, encoding);
            httpPost.setEntity(postEntity);
        }
        CloseableHttpResponse response = null;
        try {
            response = (CloseableHttpResponse) httpClient.execute(httpPost);
            resp = EntityUtils.toString(response.getEntity(), encoding);
        } catch (Exception e) {
            // log
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    // log
                    e.printStackTrace();
                }
            }
        }
        return resp;
    }


    /**
     * 发送post请求
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return
     */
    public static String sendPost(String url, Map<String, String> params) {
        Charset encoding = Charset.forName("utf8");
        return sendPost(defaultClient, url, params, encoding);
    }

    /**
     * 发送get请求
     *
     * @param url    请求地址
     * @param param 请求参数
     * @param proxy 代理设置
     * @return
     */
    public static String sendGet(String url, String param, Proxy proxy) {
        String result = "";
        BufferedReader in = null;
        try {
//            Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP,new InetSocketAddress("127.0.0.1", Integer.valueOf("1080")));
            String urlNameString = url;
            if (param != null && !param.equals("")){
                urlNameString = url + "?" + param;
            }
            URL  realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection;
            if (proxy != null) {
                connection = realUrl.openConnection(proxy);
            }else {
                connection = realUrl.openConnection();
            }

            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    public static String sendGet(String url, String param){
        return sendGet(url, param, null);
    }

    public static String sendGet(String url, Map<String, Object> params) {
        return sendGet(url, buildRequestParams(params));
    }

    public static String buildRequestParams(Map<String, Object> params) {
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

    public static void main(String[] args) {
        System.err.println("008613546464646".substring(4));
    }
}