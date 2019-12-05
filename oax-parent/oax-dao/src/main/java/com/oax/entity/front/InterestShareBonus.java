package com.oax.entity.front;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class InterestShareBonus implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer lockPositionId;
    private Integer userId;
    private Integer coinId;
    private BigDecimal qty;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLockPositionId() {
        return lockPositionId;
    }

    public void setLockPositionId(Integer lockPositionId) {
        this.lockPositionId = lockPositionId;
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

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "InterestShareBonus{" +
                "id=" + id +
                ", lockPositionId=" + lockPositionId +
                ", userId=" + userId +
                ", coinId=" + coinId +
                ", qty=" + qty +
                ", createTime=" + createTime +
                '}';
    }
}
