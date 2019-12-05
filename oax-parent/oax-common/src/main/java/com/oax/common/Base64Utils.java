package com.oax.common;

import java.io.UnsupportedEncodingException;

import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/30
 * Time: 15:33
 * Base64加密解密工具类
 */
@Slf4j
public class Base64Utils {

    //加密
    public static String getBase64(String str){
        byte[] b=null;
        String s=null;
        try {
            b = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(b!=null){
            s=new BASE64Encoder().encode(b);
        }
        return s;
    }


    // 解密
    public static String getFromBase64(String s) {
        byte[] b = null;
        String result = null;
        if (s != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                b = decoder.decodeBuffer(s);
                result = new String(b, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void main(String[] args) {


        String s = "sdfjl441s";

        String base64 = getBase64(getBase64(s));
        log.info("加密后::{}",base64);

        String fromBase64 = getFromBase64(getFromBase64(base64));

        log.info("解密后::{}",fromBase64);

    }

}
