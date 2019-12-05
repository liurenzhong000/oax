package com.oax.entity.front;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
//币种锁仓参数配置model

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * xiangwh
 */
public class CoinLockConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer coinId;
    private String coinName;
    private BigDecimal standardLockQty;
    private BigDecimal interestRate;
    private Integer isShow;
    private String ratio;
    private String image;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCoinId() {
        return coinId;
    }

    public void setCoinId(Integer coinId) {
        this.coinId = coinId;
    }

    public BigDecimal getStandardLockQty() {
        return standardLockQty;
    }

    public void setStandardLockQty(BigDecimal standardLockQty) {
        this.standardLockQty = standardLockQty;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "CoinLockConfig{" +
                "id=" + id +
                ", coinId=" + coinId +
                ", coinName=" + coinName +
                ", standardLockQty=" + standardLockQty +
                ", interestRate=" + interestRate +
                ", isShow=" + isShow +
                ", ratio='" + ratio + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", image=" + image +
                '}';
    }
}
