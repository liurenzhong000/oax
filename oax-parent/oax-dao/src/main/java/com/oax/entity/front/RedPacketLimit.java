package com.oax.entity.front;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Transient;

/**
 * red_packet_limit
 * @author 
 */
public class RedPacketLimit implements Serializable {
    private Integer id;

    /**
     * 1 普通红包 2拼手气红包
     */
    @NotNull(message = "type不能为null")
    private Integer type;

    /**
     * 币id
     */
    @NotNull(message = "币种id不能为null")
    private Integer coinId;

    /**
     * 红包限额 （普通红包：单个限额 拼手气红：总限额）
     */
    @NotNull(message = "红包限额不能为null")
    private BigDecimal limitCoinQty;

    /**
     * 限发红包数
     */
    @NotNull(message = "限发红包数不能为空")
    private Integer limitPacketQty;

    private Date createTime;

    private Date updateTime;

    @Transient
    private String coinName;

    private static final long serialVersionUID = 1L;

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

    public BigDecimal getLimitCoinQty() {
        return limitCoinQty;
    }

    public void setLimitCoinQty(BigDecimal limitCoinQty) {
        this.limitCoinQty = limitCoinQty;
    }

    public Integer getLimitPacketQty() {
        return limitPacketQty;
    }

    public void setLimitPacketQty(Integer limitPacketQty) {
        this.limitPacketQty = limitPacketQty;
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
}