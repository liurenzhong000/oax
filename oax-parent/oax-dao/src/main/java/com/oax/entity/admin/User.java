/*
 *
 * User.java
 * Copyright(C) 2018 oax公司
 * @date 2018-05-29
 */
package com.oax.entity.admin;

import java.io.Serializable;

import org.crazycake.shiro.AuthCachePrincipal;
import org.hibernate.validator.constraints.NotEmpty;

public class User implements Serializable, AuthCachePrincipal {
    /**
     *
     */
    private Integer id;

    /**
     *
     */
    @NotEmpty(message = "用户名不能为空")
    private String username;

    /**
     *
     */
    @NotEmpty(message = "密码不能为空")
    private String password;

    /**
     * 是否启用
     */
    private Integer enable;

    /**
     * 用户名称
     */
    @NotEmpty(message = "用户名不能为空")
    private String name;

    /**
     * 手机号 不能为null
     */
    @NotEmpty(message = "手机号不能为空")
    private String phone;

    /**
     * 邮箱
     */
    private String email;
    /**
     * 用户等级
     */
    private int level;

    /**
     * 谷歌验证码
     */
    private String googleCode;

    public String getGoogleCode() {
        return googleCode;
    }

    public void setGoogleCode(String googleCode) {
        this.googleCode = googleCode;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * 是否启用
     *
     * @return enable 是否启用
     */
    public Integer getEnable() {
        return enable;
    }

    /**
     * 是否启用
     *
     * @param enable 是否启用
     */
    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getAuthCacheKey() {
        return getUsername();
    }
}