package com.oax.entity.front;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TradeSnapshot implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer marketId;
    private Date beginTime;
    private Date endTime;
    private BigDecimal tradeQty;
    private BigDecimal feeToETH;
    private BigDecimal feeToX;
    private BigDecimal avgFeeBackX;
    private Date createTime;
    private Integer status;

    private BigDecimal feeToLeftCoin;
    private BigDecimal feeToRightCoin;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMarketId() {
        return marketId;
    }

    public void setMarketId(Integer marketId) {
        this.marketId = marketId;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getTradeQty() {
        return tradeQty;
    }

    public void setTradeQty(BigDecimal tradeQty) {
        this.tradeQty = tradeQty;
    }

    public BigDecimal getFeeToETH() {
        return feeToETH;
    }

    public void setFeeToETH(BigDecimal feeToETH) {
        this.feeToETH = feeToETH;
    }

    public BigDecimal getFeeToX() {
        return feeToX;
    }

    public void setFeeToX(BigDecimal feeToX) {
        this.feeToX = feeToX;
    }

    public BigDecimal getAvgFeeBackX() {
        return avgFeeBackX;
    }

    public void setAvgFeeBackX(BigDecimal avgFeeBackX) {
        this.avgFeeBackX = avgFeeBackX;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getFeeToLeftCoin() {
        return feeToLeftCoin;
    }

    public void setFeeToLeftCoin(BigDecimal feeToLeftCoin) {
        this.feeToLeftCoin = feeToLeftCoin;
    }

    public BigDecimal getFeeToRightCoin() {
        return feeToRightCoin;
    }

    public void setFeeToRightCoin(BigDecimal feeToRightCoin) {
        this.feeToRightCoin = feeToRightCoin;
    }

    @Override
    public String toString() {
        return "TradeSnapshot{" +
                "id=" + id +
                ", marketId=" + marketId +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", tradeQty=" + tradeQty +
                ", feeToETH=" + feeToETH +
                ", feeToX=" + feeToX +
                ", avgFeeBackX=" + avgFeeBackX +
                ", createTime=" + createTime +
                ", status=" + status +
                ", feeToLeftCoin=" + feeToLeftCoin +
                ", feeToRightCoin=" + feeToRightCoin +
                '}';
    }
}
