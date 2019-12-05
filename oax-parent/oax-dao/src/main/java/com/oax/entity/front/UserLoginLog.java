package com.oax.entity.front;

import java.io.Serializable;
import java.util.Date;

/**
 * user_login_log
 *
 * @author
 */
public class UserLoginLog implements Serializable {
    private Integer id;

    private Integer userId;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 登录地区
     */
    private String address;

    /**
     * 登录来源 1 pc  2 ios  3 android
     */
    private Integer source;

    /**
     * 登录时间
     */
    private Date loginTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }
}