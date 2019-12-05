package com.oax.entity.front;

import java.util.Date;

public class Member {
    private Integer id;

    private String phone;

    private String email;

    private String password;

    private String transactionPassword;

    private Integer needTransactionPassword;

    private Integer verificationType;

    private Integer lockStatus;

    private Integer checkStatus;

    private String idName;

    private String idNo;

    private String idImageA;

    private String idImageB;

    private String code;

    private Integer fromUserId;

    private Date registerTime;

    private Date updateTime;

    private Integer level;

    private Integer source;

    private String googleKey;

    private Integer emailStatus;

    private Integer phoneStatus;

    private Integer googleStatus;

    private String country;

    private Integer registerType;

    private String apiKey;

    private Integer type;

    private Boolean merchant;

    /**资金是否冻结不可用，默认为false*/
    private Boolean freezing;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTransactionPassword() {
        return transactionPassword;
    }

    public void setTransactionPassword(String transactionPassword) {
        this.transactionPassword = transactionPassword;
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

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getIdName() {
        return idName;
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Integer fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", transactionPassword='" + transactionPassword + '\'' +
                ", needTransactionPassword=" + needTransactionPassword +
                ", verificationType=" + verificationType +
                ", lockStatus=" + lockStatus +
                ", checkStatus=" + checkStatus +
                ", idName='" + idName + '\'' +
                ", idNo='" + idNo + '\'' +
                ", idImageA='" + idImageA + '\'' +
                ", idImageB='" + idImageB + '\'' +
                ", code='" + code + '\'' +
                ", fromUserId=" + fromUserId +
                ", registerTime=" + registerTime +
                ", updateTime=" + updateTime +
                ", level=" + level +
                ", source=" + source +
                ", googleKey='" + googleKey + '\'' +
                ", emailStatus=" + emailStatus +
                ", phoneStatus=" + phoneStatus +
                ", googleStatus=" + googleStatus +
                ", country='" + country + '\'' +
                ", registerType=" + registerType +
                ", apiKey='" + apiKey + '\'' +
                ", type=" + type +
                ", merchant=" + merchant +
                ", freezing=" + freezing +
                '}';
    }

    public String getGoogleKey() {
        return googleKey;
    }

    public void setGoogleKey(String googleKey) {
        this.googleKey = googleKey;
    }

    public Integer getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(Integer emailStatus) {
        this.emailStatus = emailStatus;
    }

    public Integer getPhoneStatus() {
        return phoneStatus;
    }

    public void setPhoneStatus(Integer phoneStatus) {
        this.phoneStatus = phoneStatus;
    }

    public Integer getGoogleStatus() {
        return googleStatus;
    }

    public void setGoogleStatus(Integer googleStatus) {
        this.googleStatus = googleStatus;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getNeedTransactionPassword() {
        return needTransactionPassword;
    }

    public void setNeedTransactionPassword(Integer needTransactionPassword) {
        this.needTransactionPassword = needTransactionPassword;
    }

    public Integer getRegisterType() {
        return registerType;
    }

    public void setRegisterType(Integer registerType) {
        this.registerType = registerType;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Boolean getMerchant() {
        return merchant;
    }

    public void setMerchant(Boolean merchant) {
        this.merchant = merchant;
    }

    public Boolean getFreezing() {
        return freezing;
    }

    public void setFreezing(Boolean freezing) {
        this.freezing = freezing;
    }
}