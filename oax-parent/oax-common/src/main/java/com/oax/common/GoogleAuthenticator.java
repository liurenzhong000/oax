package com.oax.common;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Base64; 

public class GoogleAuthenticator {

	// 生成的key长度( Generate secret key length)  
    public static final int SECRET_SIZE = 10;  
  
    public static final String SEED = "g8GjEvTbW5oVSV7avL47357438reyhreyuryetredLDVKs2m0QN7vxRs2im5MDaNCWGmcD2rvcZx";  
    // Java实现随机数算法  
    public static final String RANDOM_NUMBER_ALGORITHM = "SHA1PRNG";  
     // 最多可偏移的时间  
    int window_size = 3; // default 3 - max 17  
  
    /** 
     * @param s     
     */  
    public void setWindowSize(int s) {  
        if (s >= 1 && s <= 17)  
            window_size = s;  
    }  
  
    /** 
     *
     * 生成一个随机秘钥 
     * @return secret key 
     */  
    public static String generateSecretKey() {  
        SecureRandom sr = null;  
        try {  
            sr = SecureRandom.getInstance(RANDOM_NUMBER_ALGORITHM);  
            sr.setSeed(Base64.decodeBase64(SEED));  
            byte[] buffer = sr.generateSeed(SECRET_SIZE);  
            Base32 codec = new Base32();  
            byte[] bEncodedKey = codec.encode(buffer);  
            String encodedKey = new String(bEncodedKey);  
            return encodedKey;  
        } catch (NoSuchAlgorithmException e) {  
        }  
        return null;  
    }  
  
    /** 
     * @param user 
     * @param host 
     * @param secret 
     * @return the URL for the QR code to scan 
     */  
    public static String getQRBarcodeURL(String user,String secret) {  
        String format = "https://www.google.com/chart?chs=200x200&chld=M|0&cht=qr&chl=otpauth://totp/"+user+"%3Fsecret%3D"+secret+"%26issuer%3Dxbtcoin.xbtc.cx";
        return format;  
    }  
  
    /** 
     * 生成一个google身份验证器，识别的字符串，只需要把该方法返回值生成二维码扫描就可以了。 
     * @param user 
     * @param secret 
     * @return 
     */  
    public static String getQRBarcode(String user, String secret) {  
        String format = "otpauth://totp/"+user+"%3Fsecret%3D"+secret+"%26issuer%3Dxbtcoin.xbtc.cx";  
        return format;  
    }  
  
    /** 
     * @param secret 
     * @param code 
     * @param t 
     * @return 
     */  
    public  boolean checkCode(String secret, long code, long timeMsec) {  
        Base32 codec = new Base32();  
        byte[] decodedKey = codec.decode(secret);  
        long t = (timeMsec / 1000L) / 30L;  
        for (int i = -window_size; i <= window_size; ++i) {  
            long hash;  
            try {  
                hash = verify_code(decodedKey, t + i);  
            } catch (Exception e) {  
                e.printStackTrace();  
                throw new RuntimeException(e.getMessage());  
            }  
            if (hash == code) {  
                return true;  
            }  
        }  
        return false;  
    }  
  
    private static int verify_code(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException {  
        byte[] data = new byte[8];  
        long value = t;  
        for (int i = 8; i-- > 0; value >>>= 8) {  
            data[i] = (byte) value;  
        }  
        SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");  
        Mac mac = Mac.getInstance("HmacSHA1");  
        mac.init(signKey);  
        byte[] hash = mac.doFinal(data);  
        int offset = hash[20 - 1] & 0xF;  
        long truncatedHash = 0;  
        for (int i = 0; i < 4; ++i) {  
            truncatedHash <<= 8;  
            truncatedHash |= (hash[offset + i] & 0xFF);  
        }  
        truncatedHash &= 0x7FFFFFFF;  
        truncatedHash %= 1000000;  
        return (int) truncatedHash;  
    }  
}
