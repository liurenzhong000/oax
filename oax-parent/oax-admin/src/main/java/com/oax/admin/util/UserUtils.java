package com.oax.admin.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.oax.entity.admin.User;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/6
 * Time: 14:35
 * 后台获取用户信息 工具类
 */
public class UserUtils {

    public static User getShiroUser() {
        Subject subject = SecurityUtils.getSubject();
        return (User) subject.getPrincipal();
    }

    public static void logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }
}
