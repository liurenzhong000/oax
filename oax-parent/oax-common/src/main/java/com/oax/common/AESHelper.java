package com.oax.common;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;

public class AESHelper {

    public static byte[] encrypt(byte[] bytes, String key) {
        try {
            byte[] keyBytes = getKeyBytes(key);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyBytes, "AES"), new IvParameterSpec(keyBytes));
            return cipher.doFinal(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String encrypt(String data, String key, CodecEncoder encoder) {
        byte[] bytes = encrypt(data.getBytes(), key);
        String result = null;
        switch (encoder) {
            case BASE64:
                result = new String(Base64.getEncoder().encode(bytes));
                break;
            case HEX:
                result = HexHelper.toHexString(bytes);
                break;
            case WEB_SAVE_BASE64:
                result = new String(Base64.getUrlEncoder().encode(bytes));
                break;
            default:
                result = new String(Base64.getEncoder().encode(bytes));
                break;
        }
        return result;
    }

    public static String encrypt(String data, String key) {
        return encrypt(data, key, CodecEncoder.BASE64);
    }

    public static byte[] decrypt(byte[] encryptBytes, String key) {
        try {
            byte[] keyBytes = getKeyBytes(key);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes, "AES"), new IvParameterSpec(keyBytes));
            return cipher.doFinal(encryptBytes);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String decrypt(String encryptString, String key, CodecEncoder encoder) {
        byte[] bytes = null;
        switch (encoder) {
            case BASE64:
                bytes = decrypt(Base64.getDecoder().decode(encryptString), key);
                break;
            case HEX:
                bytes = decrypt(HexHelper.toBytes(encryptString), key);
                break;
            case WEB_SAVE_BASE64:
                bytes = decrypt(Base64.getUrlDecoder().decode(encryptString), key);
                break;
            default:
                bytes = decrypt(Base64.getDecoder().decode(encryptString), key);
                break;
        }
        return new String(bytes);
    }

    public static String decrypt(String encryptString, String key) {
        return decrypt(encryptString, key, CodecEncoder.BASE64);
    }

    /**
     * 文件加密
     *
     * @param key
     * @param sourceFilePath
     * @param destFilePath
     */
    public static void fileEncrypt(String key, String sourceFilePath, String destFilePath) {
        Path path = Paths.get(sourceFilePath);
        Path destPath = Paths.get(destFilePath);
        try {
            byte[] sourceBytes = Files.readAllBytes(path);
            byte[] destBytes = encrypt(sourceBytes, key);
            Files.write(destPath, destBytes);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    /**
     * 文件解密
     *
     * @param key
     * @param sourceFilePath
     * @param destFilePath
     */
    public static void fileDecrypt(String key, String sourceFilePath, String destFilePath) {
        Path path = Paths.get(sourceFilePath);
        Path destPath = Paths.get(destFilePath);
        try {
            byte[] sourceBytes = Files.readAllBytes(path);
            byte[] destBytes = decrypt(sourceBytes, key);
            Files.write(destPath, destBytes);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    // 大于16字节只取前面16个字节,小于16字节后面补0
    private static byte[] getKeyBytes(String key) {
        byte[] bytes = key.getBytes();
        return bytes.length == 16 ? bytes : Arrays.copyOf(bytes, 16);
    }

    public static void main(String[] args) {
        String encrypted = AESHelper.encrypt("323433-20181541", "!@#eW*$%!~", CodecEncoder.WEB_SAVE_BASE64);
        System.out.println(encrypted.length()+":"+encrypted);
    }
}