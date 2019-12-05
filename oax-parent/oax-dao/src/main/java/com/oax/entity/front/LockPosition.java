package com.oax.entity.front;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class LockPosition implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer userId;
    private Integer coinId;
    private BigDecimal lockQty;
    private Integer lockDays;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deblockingTime;

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

    public Integer getCoinId() {
        return coinId;
    }

    public void setCoinId(Integer coinId) {
        this.coinId = coinId;
    }

    public BigDecimal getLockQty() {
        return lockQty;
    }

    public void setLockQty(BigDecimal lockQty) {
        this.lockQty = lockQty;
    }

    public Integer getLockDays() {
        return lockDays;
    }

    public void setLockDays(Integer lockDays) {
        this.lockDays = lockDays;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getDeblockingTime() {
        return deblockingTime;
    }

    public void setDeblockingTime(Date deblockingTime) {
        this.deblockingTime = deblockingTime;
    }

    @Override
    public String toString() {
        return "LockPosition{" +
                "id=" + id +
                ", userId=" + userId +
                ", coinId=" + coinId +
                ", lockQty=" + lockQty +
                ", lockDays=" + lockDays +
                ", status=" + status +
                ", createTime=" + createTime +
                ", deblockingTime=" + deblockingTime +
                '}';
    }
}
