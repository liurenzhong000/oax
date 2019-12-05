package com.oax.entity.front;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * red_packet
 * @author
 */
public class RedPacket implements Serializable {
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 1 普通红包 2 拼手气红包
     */
    private Integer type;

    private Integer coinId;

    /**
     * 币名
     */
    private String coinName;

    /**
     * 币总数
     */
    private BigDecimal totalCoinQty;

    /**
     * 总价值人民币
     */
    private BigDecimal totalCny;

    /**
     * 红包总个数
     */
    private Integer totalPacketQty;

    /**
     * 祝福语
     */
    private String wishWords;

    /**
     * 已领币数
     */
    private BigDecimal grabCoinQty;

    /**
     * 已领红包个数
     */
    private Integer grabPacketQty;

    private Integer isRefund;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expireTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public String getWishWords() {
        return wishWords;
    }

    public void setWishWords(String wishWords) {
        this.wishWords = wishWords;
    }

    public BigDecimal getGrabCoinQty() {
        return grabCoinQty;
    }

    public void setGrabCoinQty(BigDecimal grabCoinQty) {
        this.grabCoinQty = grabCoinQty;
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

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    private String accountNumber;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Integer getIsRefund() {
        return isRefund;
    }

    public void setIsRefund(Integer isRefund) {
        this.isRefund = isRefund;
    }

    @Override
    public String toString() {
        return "RedPacket{" +
                "id=" + id +
                ", userId=" + userId +
                ", type=" + type +
                ", coinId=" + coinId +
                ", coinName='" + coinName + '\'' +
                ", totalCoinQty=" + totalCoinQty +
                ", totalCny=" + totalCny +
                ", totalPacketQty=" + totalPacketQty +
                ", wishWords='" + wishWords + '\'' +
                ", grabCoinQty=" + grabCoinQty +
                ", grabPacketQty=" + grabPacketQty +
                ", isRefund=" + isRefund +
                ", createTime=" + createTime +
                ", expireTime=" + expireTime +
                ", updateTime=" + updateTime +
                ", accountNumber='" + accountNumber + '\'' +
                '}';
    }
}