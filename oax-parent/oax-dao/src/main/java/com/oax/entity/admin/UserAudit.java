package com.oax.entity.admin;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/*
 * 用户审核需要的数据
 */
public class UserAudit {
    //认证等级 二级
    public static final Integer LEVEL_TYPE_TWO = 2;
    //认证等级 三级
    public static final Integer LEVEL_TYPE_THREE = 3;
    //用户id
    private Integer userId;
    //证件姓名
    private String name;
    // 认证类型  1身份证
    private Integer verificationType;
    // 证件号
    private String cardNo;
    //证件正面
    private String idImageA;
    //证件背面
    private String idImageB;
    //处理员ID
    private Integer adminId;
    //处理员姓名
    private String adminName;
    //处理时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date disposeTime;
    //描述
    private String describes;

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

    public Integer getVerificationType() {
        return verificationType;
    }

    public void setVerificationType(Integer verificationType) {
        this.verificationType = verificationType;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getIdImageA() {
        return idImageA;
    }

    public void setIdImageA(String idImageA) {
        this.idImageA = idImageA;
    }

    public String getIdImageB() {
        return idImageB;
    }

    public void setIdImageB(String idImageB) {
        this.idImageB = idImageB;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public Date getDisposeTime() {
        return disposeTime;
    }

    public void setDisposeTime(Date disposeTime) {
        this.disposeTime = disposeTime;
    }

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }

}
