package com.oax.entity.admin.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 用户列表
 */
public class UserDetailsUpVo {
    //用户ID
    private Integer id;
    //用户姓名
    private String name;
    //用户手机
    private String phone;
    //用户邮箱
    private String email;
    //转币限额
    private String quota;
    //锁定状态 是否被锁定 0 未锁定 1锁定
    private Integer lockStatus;
    //注册时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date registerTime;
    //认证等级 等级 0未激活 1 2 3
    private Integer level;
    //证件类型  1身份证
    private Integer verificationType;
    //证件号码
    private String idNo;

    private Integer registerType;

    public Integer getRegisterType() {
        return registerType;
    }

    public void setRegisterType(Integer registerType) {
        this.registerType = registerType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getQuota() {
        return quota;
    }

    public void setQuota(String quota) {
        this.quota = quota;
    }

    public Integer getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(Integer lockStatus) {
        this.lockStatus = lockStatus;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getVerificationType() {
        return verificationType;
    }

    public void setVerificationType(Integer verificationType) {
        this.verificationType = verificationType;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }
}
