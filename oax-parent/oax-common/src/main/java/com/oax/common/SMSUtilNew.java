package com.oax.common;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created on 17/6/7. 短信API产品的DEMO程序,工程中包含了一个SmsDemo类，直接通过
 * 执行main函数即可体验短信产品API功能(只需要将AK替换成开通了云通信-短信产品功能的AK即可) 工程依赖了2个jar包(存放在工程的libs目录下)
 * 1:aliyun-java-sdk-core.jar 2:aliyun-java-sdk-dysmsapi.jar
 *
 * 备注:Demo工程编码采用UTF-8 国际短信发送请勿参照此DEMO
 */
@Slf4j
public class SMSUtilNew {

    /*
     * //产品名称:云通信短信API产品,开发者无需替换 ----------------------------------- 阿里云短信接口 static
     * final String PRODUCT = "Dysmsapi"; //产品域名,开发者无需替换 static final String DOMAIN
     * = "dysmsapi.aliyuncs.com"; static final String ACCESS_KEY_ID =
     * "LTAIlaVL48fVXoY5"; static final String ACCESS_KEY_SECRET =
     * "AUo55NfMXYC81iQM6pSXs1uLXAkrKr";
     */

    // ----------------------------------- 麦讯通
    static final String UserID = "195831";
    static final String Account = "admin";
    static final String Password = "329437";

    public static String sendCodeChinaPhone(String phonenNo, String templateParam){
        if(phonenNo.startsWith("0086")) {
            phonenNo = phonenNo.substring(4);
        }
        return sendCode(phonenNo, templateParam);
    }
    public static String sendCode(String phonenNo, String templateParam) {
        Map<String, String> params = new HashMap<String, String>();

        params.put("UserID", UserID);
        params.put("Account", Account);
        params.put("Password", Password);
        params.put("Phones",phonenNo);
        params.put("SendType", "1");
        params.put("SendTime", "");// df.format(date)
        params.put("PostFixNumber", "");
        params.put("Content", templateParam + "【新比特XBTC】");

        String response = HttpRequestUtil.sendPost("http://113.106.16.55:8080/GateWay/Services.asmx/DirectSend?",
                params);
        log.info("发送短信验证码返回数据："+response);
        String retCode = null;
        String str;
        try {
            str = new String(response.getBytes(), "GBK");

            if (str.contains("GBK")) {
                str = str.replaceAll("GBK", "utf-8");
            }
            int beginPoint = str.indexOf("<RetCode>");
            int endPoint = str.indexOf("</RetCode>");
            retCode = str.substring(beginPoint + 9, endPoint);
            System.out.println(retCode);

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return retCode;
    }

    public static String getSmsCode(int charCount) {
        String charValue = "";
        for (int i = 0; i < charCount; i++) {
            char c = (char) (randomInt(0, 10) + '0');
            charValue += String.valueOf(c);
        }
        return charValue;
    }

    public static int randomInt(int from, int to) {
        Random r = new Random();
        return from + r.nextInt(to - from);
    }

    /**
     * 测试
     */
    /*
     * public static void main(String[] args) { new Thread(new Runnable() {
     *
     * @Override public void run() { JSONObject jsonObject = new JSONObject();
     * jsonObject.put("name","aaa"); jsonObject.put("qty",1.2); String s =
     * SMSUtil.sendCode("17665329290", "SMS_138072444", jsonObject.toJSONString());
     * System.out.println(s); } }).start(); }
     */

    public static void main(String[] args) {

        Map<String, String> params = new HashMap<String, String>();

        params.put("UserID", UserID);
        params.put("Account", Account);
        params.put("Password", Password);
        params.put("Phones", "18296799192");
        params.put("SendType", "1");
        params.put("SendTime", "");// df.format(date)
        params.put("PostFixNumber", "");
        params.put("Content", "1233【新比特XBTC】");

        String response = HttpRequestUtil.sendPost("http://113.106.16.55:8080/GateWay/Services.asmx/DirectSend?",
                params);

        String str;
        try {
            str = new String(response.getBytes(), "GBK");

            if (str.contains("GBK")) {
                str = str.replaceAll("GBK", "utf-8");
            }
            int beginPoint = str.indexOf("<RetCode>");
            int endPoint = str.indexOf("</RetCode>");
            String RetCode = str.substring(beginPoint + 9, endPoint);
            System.out.println(RetCode);

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
