package com.oax.common.util.string;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @Auther: hyp
 * @Date: 2019/1/21 17:00
 * @Description: 字符串相关验证工具类
 */
public class VerificationHelper {

    //0086开头 或没有开头
    public static String hidePhone(String phone){
        if (StringUtils.isBlank(phone)) {
            return "";
        }
        if(phone.length() == 15){
            return phone.replaceAll("(\\d{4})(\\d{3})\\d{4}(\\d{4})","$2****$3");
        } else if (phone.length() == 11){
            return phone.replaceAll("^(\\d{3})\\d{4}(\\d+)","$1****$2");
        }
        return "";
    }

    public static String hideEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return "";
        }
        return email.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4");
    }

    public static String hidePhoneAndEmail(String phoneOrEmail) {
        if (isPhone(phoneOrEmail)) {
            return hidePhone(phoneOrEmail);
        } else {
            return hideEmail(phoneOrEmail);
        }
    }

    public static boolean isPhone(String phoneOrEmail){
        //TODO 判断是否为手机号
        if (phoneOrEmail.contains("@")) {
            return false;
        }
        return true;
    }

    /**
     * 返回手机号码
     */
    private static String[] telFirst="134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");
    public static String getRandomPhone() {
        int index= RandomUtils.nextInt(1, telFirst.length);
        String first = telFirst[index];
        String second = String.valueOf(RandomUtils.nextInt(1,8999)+1000);
        String third = String.valueOf(RandomUtils.nextInt(1,8999)+1000);
        return first + second + third;
    }

    public static void main(String[] args) {
        System.out.println(getRandomPhone());
        System.out.println(hidePhoneAndEmail("1024de7@163.com"));
        System.out.println(hidePhone("18396799152"));
    }
}
