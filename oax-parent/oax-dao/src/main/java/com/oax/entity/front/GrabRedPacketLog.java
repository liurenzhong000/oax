package com.oax.entity.front;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.annotation.Transient;

/**
 * grab_red_packet_log
 * @author 
 */
public class GrabRedPacketLog implements Serializable {
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 红包id
     */
    private Integer redPacketId;

    /**
     * 币id
     */
    private Integer coinId;

    /**
     * 币名称
     */
    private String coinName;

    /**
     * 币数量
     */
    private BigDecimal coinQty;

    /**
     * 价值人民币
     */
    private BigDecimal cny;

    /**
     * 领取途径
     */
    private String sources;

    /**
     * 创建时间
     */
    private Date createTime;


    @Transient
    private String phone;
    @Transient
    private String email;

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

    public Integer getRedPacketId() {
        return redPacketId;
    }

    public void setRedPacketId(Integer redPacketId) {
        this.redPacketId = redPacketId;
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

    public BigDecimal getCoinQty() {
        return coinQty;
    }

    public void setCoinQty(BigDecimal coinQty) {
        this.coinQty = coinQty;
    }

    public BigDecimal getCny() {
        return cny;
    }

    public void setCny(BigDecimal cny) {
        this.cny = cny;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSources() {
        return sources;
    }

    public void setSources(String sources) {
        this.sources = sources;
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

    @Override
    public String

    toString() {
        return "GrabRedPacketLog{" +
                "id=" + id +
                ", userId=" + userId +
                ", redPacketId=" + redPacketId +
                ", coinId=" + coinId +
                ", coinName='" + coinName + '\'' +
                ", coinQty=" + coinQty +
                ", cny=" + cny +
                ", createTime=" + createTime +
                ", sources='" + sources + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}