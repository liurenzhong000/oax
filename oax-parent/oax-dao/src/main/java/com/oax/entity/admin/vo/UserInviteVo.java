package com.oax.entity.admin.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UserInviteVo {
    //用户id
    private Integer userId;
    //用户名
    private String name;
    //手机号
    private String phone;
    //邮箱
    private String email;
    //证件类型 1身份证
    private int verificationType;
    //证件号
    private String idNo;
    //注册时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date registerTime;
    //是否被锁定 0 未锁定 1锁定
    private Integer lockStatus;
    //等级 0未激活 1 2 3
    private Integer level;
    //lV2认证时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date checkTime;

    //层级
    private String tier;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getVerificationType() {
        return verificationType;
    }

    public void setVerificationType(int verificationType) {
        this.verificationType = verificationType;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Integer getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(Integer lockStatus) {
        this.lockStatus = lockStatus;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }
}
