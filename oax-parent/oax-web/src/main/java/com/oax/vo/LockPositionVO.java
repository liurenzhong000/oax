package com.oax.vo;

import java.math.BigDecimal;

public class LockPositionVO {
    private Integer userId;
    private Integer coinId;
    private BigDecimal lockQty;
    private Integer lockDays;

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

    @Override
    public String toString() {
        return "LockPositionVO{" +
                "userId=" + userId +
                ", coinId=" + coinId +
                ", lockQty=" + lockQty +
                ", lockDays=" + lockDays +
                '}';
    }
}
