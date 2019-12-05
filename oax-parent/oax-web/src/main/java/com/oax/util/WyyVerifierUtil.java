package com.oax.util;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.oax.common.HttpRequestUtil;

public class WyyVerifierUtil {
	//验证码id
	private  String captchaId;
	
	//密钥id
	private String secretId;
	
	//密钥key
	private String secretKey;
	
	//二次验证接口请求地址
	private String verifyUrl;
	
	//版本 
	private String version;

	
    public WyyVerifierUtil(String captchaId, String secretId, String secretKey, String verifyUrl, String version) {
		super();
		this.captchaId = captchaId;
		this.secretId = secretId;
		this.secretKey = secretKey;
		this.verifyUrl = verifyUrl;
		this.version = version;
	}


	/**
     * 二次验证
     * @param validate 验证码组件提交上来的NECaptchaValidate值
     * @param user     用户
     * @return
     */
    public boolean verify(String validate, String user) { 
        if (StringUtils.isEmpty(validate) || StringUtils.equals(validate, "null")) {
            return false;
        }
        
        user = (user == null) ? "" : user;
        Map<String, String> params = new HashMap<String, String>();
        params.put("captchaId", captchaId);
        params.put("validate", validate);
        params.put("user", user);
        
        // 公共参数
        params.put("secretId", secretId);
        params.put("version", version);
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));
        params.put("nonce", String.valueOf(ThreadLocalRandom.current().nextInt()));
        
        // 计算请求参数签名信息
        String signature = sign(secretKey, params);
        params.put("signature", signature);

        String resp = HttpRequestUtil.sendPost(verifyUrl, params);
        System.out.println("resp = " + resp+"&&&"+validate);
        return verifyRet(resp);
    }

    
    /**
     * 生成签名信息
     * @param secretKey 验证码私钥
     * @param params    接口请求参数名和参数值map，不包括signature参数名
     * @return
     */
    public static String sign(String secretKey, Map<String, String> params) {
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        StringBuffer sb = new StringBuffer();
        for (String key : keys) {
            sb.append(key).append(params.get(key));
        }
        sb.append(secretKey);
        try {
            return DigestUtils.md5Hex(sb.toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();// 一般编码都支持的。。
        }
        return null;
    }

    
    /**
     * 验证返回结果
     * @param resp
     * @return
     */
    private boolean verifyRet(String resp) {
        if (StringUtils.isEmpty(resp)) {
            return false;
        }
        try {
            JSONObject j = JSONObject.parseObject(resp);
            return j.getBoolean("result");
        } catch (Exception e) {
            return false;
        }
    }
}
