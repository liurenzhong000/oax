/**
 *
 */
package com.oax.entity.admin;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 后台系统用户列表
 */
public class UserInfo implements Serializable {
    //默认等级
    public static final Integer LEVEL_TYPE_ZERO = 0;
    //认证等级 一级
    public static final Integer LEVEL_TYPE_ONE = 1;
    //认证等级 二级
    public static final Integer LEVEL_TYPE_TWO = 2;
    //认证等级 三级
    public static final Integer LEVEL_TYPE_THREE = 3;
    //审核状态  -1未通过
    public static final Integer CHECK_STATUS_NOT_PASS = -1;
    //审核状态  0未认证
    public static final Integer CHECK_STATUS_UNVERIFIED = 0;
    //审核状态  1待审核
    public static final Integer CHECK_STATUS_CHECK_PENDING = 1;
    //审核状态  2审核通过
    public static final Integer CHECK_STATUS_PASS_THE_AUDIT = 2;
    //是否被锁定 未锁定
    public static final Integer LOCK_STATUS_ZERO = 0;
    //是否被锁定 锁定
    public static final Integer LOCK_STATUS_ONE = 1;
    //解锁成功
    public static final Integer LOCK_STATUS_TWO = 2;

    //lv2审核状态  -1未通过  0未认证  1待审核 2审核通过
    private Integer checkStatus = CHECK_STATUS_UNVERIFIED;
    //用户id
    private Integer id;
    //手机号
    private String phone;
    // 邮箱
    private String email;
    // 认证类型  1身份证
    private Integer verificationType;
    // 是否被锁定 0 未锁定 1锁定
    private Integer lockStatus;
    // 证件姓名
    private String name;
    // 证件号
    private String cardNo;
    // 注册时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date registerTime;
    // 等级 0未激活 1 2 3
    private Integer level = LEVEL_TYPE_ZERO;
    // 来源 1 pc 2 ios 3 android
    private Integer source;

    //模糊查询条件 电话/邮箱
    private String eamilOrPhone;
    //注册起始时间
    private String regBeginDate;
    //注册结束时间
    private String regEndDate;

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getVerificationType() {
        return verificationType;
    }

    public void setVerificationType(Integer verificationType) {
        this.verificationType = verificationType;
    }

    public Integer getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(Integer lockStatus) {
        this.lockStatus = lockStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getEamilOrPhone() {
        return eamilOrPhone;
    }

    public void setEamilOrPhone(String eamilOrPhone) {
        this.eamilOrPhone = eamilOrPhone;
    }

    public String getRegBeginDate() {
        return regBeginDate;
    }

    public void setRegBeginDate(String regBeginDate) {
        this.regBeginDate = regBeginDate;
    }

    public String getRegEndDate() {
        return regEndDate;
    }

    public void setRegEndDate(String regEndDate) {
        this.regEndDate = regEndDate;
    }

}
