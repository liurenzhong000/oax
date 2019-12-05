package com.oax.entity.front;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * sum_coin
 *
 * @author
 */
public class SumCoin implements Serializable {
    private Integer id;

    /**
     * 日期
     */
    private Date sumDate;

    /**
     * 币种id
     */
    private Integer coinId;

    /**
     * 充币量
     */
    private BigDecimal rechargeQty;

    /**
     * 提币量
     */
    private BigDecimal withdrawQty;

    /**
     * 提币手续费
     */
    private BigDecimal withdrawFee;

    /**
     * 交易手续费
     */
    private BigDecimal tradeFee;

    /**
     * 平台成本
     */
    private BigDecimal platformCost;

    private Date createTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getSumDate() {
        return sumDate;
    }

    public void setSumDate(Date sumDate) {
        this.sumDate = sumDate;
    }

    public Integer getCoinId() {
        return coinId;
    }

    public void setCoinId(Integer coinId) {
        this.coinId = coinId;
    }

    public BigDecimal getRechargeQty() {
        return rechargeQty;
    }

    public void setRechargeQty(BigDecimal rechargeQty) {
        this.rechargeQty = rechargeQty;
    }

    public BigDecimal getWithdrawQty() {
        return withdrawQty;
    }

    public void setWithdrawQty(BigDecimal withdrawQty) {
        this.withdrawQty = withdrawQty;
    }

    public BigDecimal getWithdrawFee() {
        return withdrawFee;
    }

    public void setWithdrawFee(BigDecimal withdrawFee) {
        this.withdrawFee = withdrawFee;
    }

    public BigDecimal getTradeFee() {
        return tradeFee;
    }

    public void setTradeFee(BigDecimal tradeFee) {
        this.tradeFee = tradeFee;
    }

    public BigDecimal getPlatformCost() {
        return platformCost;
    }

    public void setPlatformCost(BigDecimal platformCost) {
        this.platformCost = platformCost;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}