package com.oax.entity.front;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class RedPacketRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer coinId;
    /**
     *coinName
     */
    private String coinName;
    /**
     * 1 普通红包 2 拼手气红包
     */
    private Integer type;
    /**
     * 总额
     */
    private BigDecimal totalCoinQty;
    /**
     * 价值人民币
     */
    private BigDecimal totalCny;
    /**
     * 总个数
     */
    private Integer totalPacketQty;

    /**
     * 已领个数
     */
    private Integer grabPacketQty;

    private Integer expireFlag;
    private Integer takeFlag;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

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

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getTotalCoinQty() {
        return totalCoinQty;
    }

    public void setTotalCoinQty(BigDecimal totalCoinQty) {
        this.totalCoinQty = totalCoinQty;
    }

    public BigDecimal getTotalCny() {
        return totalCny;
    }

    public void setTotalCny(BigDecimal totalCny) {
        this.totalCny = totalCny;
    }

    public Integer getTotalPacketQty() {
        return totalPacketQty;
    }

    public void setTotalPacketQty(Integer totalPacketQty) {
        this.totalPacketQty = totalPacketQty;
    }

    public Integer getGrabPacketQty() {
        return grabPacketQty;
    }

    public void setGrabPacketQty(Integer grabPacketQty) {
        this.grabPacketQty = grabPacketQty;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getExpireFlag() {
        return expireFlag;
    }

    public void setExpireFlag(Integer expireFlag) {
        this.expireFlag = expireFlag;
    }

    public Integer getTakeFlag() {
        return takeFlag;
    }

    public void setTakeFlag(Integer takeFlag) {
        this.takeFlag = takeFlag;
    }

    @Override
    public String toString() {
        return "RedPacketRecord{" +
                "id=" + id +
                ", coinId=" + coinId +
                ", coinName='" + coinName + '\'' +
                ", type=" + type +
                ", totalCoinQty=" + totalCoinQty +
                ", totalCny=" + totalCny +
                ", totalPacketQty=" + totalPacketQty +
                ", grabPacketQty=" + grabPacketQty +
                ", expireFlag=" + expireFlag +
                ", takeFlag=" + takeFlag +
                ", createTime=" + createTime +
                '}';
    }
}
