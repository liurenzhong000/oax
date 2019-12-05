package com.oax.context;

/**
 * 使用threadLocal保存相关信息
 */
public class HttpContext {

    /**用户id*/
    private static final ThreadLocal<Integer> userIdThreadLocal = new ThreadLocal<>();

    /**语言*/
    private static final ThreadLocal<String> langThreadLocal = new ThreadLocal<>();


    public static Integer getUserId() {
        return userIdThreadLocal.get();
    }

    public static void saveUserId(Integer userId) {
        userIdThreadLocal.set(userId);
    }

    public static String getLang() {
        return langThreadLocal.get();
    }

    public static void saveLang(String lang) {
        langThreadLocal.set(lang);
    }

    public static void clear(){
        userIdThreadLocal.remove();
        langThreadLocal.remove();
    }
}
