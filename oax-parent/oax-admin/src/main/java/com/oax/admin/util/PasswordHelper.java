package com.oax.admin.util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import com.oax.entity.admin.User;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/5/30
 * Time: 11:57
 * <p>
 * 用户密码加密工具类
 */
public class PasswordHelper {

    //private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
    private String algorithmName = "md5";
    private int hashIterations = 2;

    public void encryptPassword(User user) {
        //String salt=randomNumberGenerator.nextBytes().toHex();
        String newPassword = new SimpleHash(algorithmName, user.getPassword(), ByteSource.Util.bytes(user.getUsername()), hashIterations).toHex();
        //String newPassword = new SimpleHash(algorithmName, user.getPassword()).toHex();
        user.setPassword(newPassword);
    }

    public static void main(String[] args) {
        PasswordHelper passwordHelper = new PasswordHelper();
        User user = new User();
        user.setUsername("hyp");
        user.setPassword("hyp123");
        passwordHelper.encryptPassword(user);
        System.out.println(user);
        System.out.println(user.getPassword());
    }
}
