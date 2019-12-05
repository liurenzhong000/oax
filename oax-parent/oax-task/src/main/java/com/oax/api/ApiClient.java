package com.oax.api;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.xml.bind.DatatypeConverter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oax.domain.*;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ApiClient {

    static final int CONN_TIMEOUT = 3;
    static final int READ_TIMEOUT = 3;
    static final int WRITE_TIMEOUT = 3;

    static final MediaType JSON = MediaType.parse("application/json");
    static final OkHttpClient client = createOkHttpClient();

    final String accessKeyId;
    final String accessKeySecret;
    final String assetPassword;

    static String API_HOST;
    static String API_URL;

    /**
     * 创建一个ApiClient实例
     *
     * @param accessKeyId     AccessKeyId
     * @param accessKeySecret AccessKeySecret
     */
    public ApiClient(String accessKeyId, String accessKeySecret) {
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.assetPassword = null;
    }

    /**
     * 创建一个ApiClient实例
     *
     * @param accessKeyId     AccessKeyId
     * @param accessKeySecret AccessKeySecret
     * @param assetPassword   AssetPassword
     */
    public ApiClient(String accessKeyId, String accessKeySecret, String assetPassword) {
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.assetPassword = assetPassword;
    }

    /**
     * 创建一个ApiClient实例
     *
     * @param accessKeyId     AccessKeyId
     * @param accessKeySecret AccessKeySecret
     * @param assetPassword   AssetPassword
     */
    public ApiClient(String accessKeyId, String accessKeySecret, String assetPassword,String apiHost,String apiUrl) {
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.assetPassword = assetPassword;
        ApiClient.API_HOST = apiHost;
        ApiClient.API_URL = apiUrl;
    }

    /**
     * GET /v1/account/accounts 查询当前用户的所有账户(即account-id)
     *
     * @return
     */
    public AccountsResponse<?> accounts() {
        AccountsResponse<?> resp = get("/v1/account/accounts", null, new TypeReference<AccountsResponse<List<Accounts>>>() {
        });
        return resp;
    }

    /**
     * 创建订单（未执行)
     *
     * @param request CreateOrderRequest object.
     * @return Order id.
     */
    public Long createOrder(CreateOrderRequest request) {
        ApiResponse<Long> resp = post("/v1/order/orders", request, new TypeReference<ApiResponse<Long>>() {});
        return resp.checkAndReturn();
    }

    /**
     * 执行订单
     *
     * @param orderId The id of created order.
     * @return Order id.
     */
    public String placeOrder(long orderId) {
        ApiResponse<String> resp = post("/v1/order/orders/" + orderId + "/place", null, new TypeReference<ApiResponse<String>>() {});
        return resp.checkAndReturn();
    }

    /**
     * GET /market/depth 获取 Market Depth 数据
     *
     * @param request
     * @return
     */
    public DepthResponse<?> depth(DepthRequest request) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("symbol", request.getSymbol());
        map.put("type", request.getType());

        DepthResponse<?> resp = get("/market/depth", map, new TypeReference<DepthResponse<List<Depth>>>() {});
        return resp;
    }

    /**
     * GET /market/trade 获取最近的交易记录
     * @param request
     * @return
     */
    public TradeResponse<?> trade(TradeRequest request){
        HashMap<String, String> map = new HashMap();
        map.put("symbol", request.getSymbol());

        TradeResponse<?> resp = get("/market/trade", map, new TypeReference<TradeResponse<List<TradeData<TradeDetail>>>>() {});

        return resp;
    }


    <T> T get(String uri, Map<String, String> params, TypeReference<T> ref) {
        if (params == null) {
            params = new HashMap<>();
        }
        return call("GET", uri, null, params, ref);
    }

    <T> T post(String uri, Object object, TypeReference<T> ref) {
        return call("POST", uri, object, new HashMap<String, String>(), ref);
    }

    <T> T call(String method, String uri, Object object, Map<String, String> params, TypeReference<T> ref) {
        ApiSignature sign = new ApiSignature();
        sign.createSignature(this.accessKeyId, this.accessKeySecret, method, API_HOST, uri, params);
        try {
            Request.Builder builder = null;
            if ("POST".equals(method)) {
                RequestBody body = RequestBody.create(JSON, DepthResponse.JsonUtil.writeValue(object));
                builder = new Request.Builder().url(API_URL + uri + "?" + toQueryString(params)).post(body);
            } else {
                builder = new Request.Builder().url(API_URL + uri + "?" + toQueryString(params)).get();
            }
            if (this.assetPassword != null) {
                builder.addHeader("AuthData", authData());
            }
            Request request = builder.build();
            Response response = client.newCall(request).execute();
            String s = response.body().string();
            return DepthResponse.JsonUtil.readValue(s, ref);
        } catch (IOException e) {
            throw new ApiException(e);
        }
    }

    String authData() {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md.update(this.assetPassword.getBytes(StandardCharsets.UTF_8));
        md.update("hello, moto".getBytes(StandardCharsets.UTF_8));
        Map<String, String> map = new HashMap<>();
        map.put("assetPwd", DatatypeConverter.printHexBinary(md.digest()).toLowerCase());
        try {
            return ApiSignature.urlEncode(DepthResponse.JsonUtil.writeValue(map));
        } catch (IOException e) {
            throw new RuntimeException("Get json failed: " + e.getMessage());
        }
    }

    String toQueryString(Map<String, String> params) {
        return String.join("&", params.entrySet().stream().map((entry) -> {
            return entry.getKey() + "=" + ApiSignature.urlEncode(entry.getValue());
        }).collect(Collectors.toList()));
    }

    static OkHttpClient createOkHttpClient() {
        return new Builder().connectTimeout(CONN_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS).writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

}