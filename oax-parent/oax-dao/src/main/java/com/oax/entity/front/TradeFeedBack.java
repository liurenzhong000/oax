package com.oax.entity.front;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TradeFeedBack implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer snapshotId;
    private Integer tradeId;
    private Integer userId;
    private BigDecimal qty;
    private BigDecimal tradeQty;
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(Integer snapshotId) {
        this.snapshotId = snapshotId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public Integer getTradeId() {
        return tradeId;
    }

    public void setTradeId(Integer tradeId) {
        this.tradeId = tradeId;
    }

    public BigDecimal getTradeQty() {
        return tradeQty;
    }

    public void setTradeQty(BigDecimal tradeQty) {
        this.tradeQty = tradeQty;
    }

    @Override
    public String toString() {
        return "TradeFeedBack{" +
                "id=" + id +
                ", snapshotId=" + snapshotId +
                ", tradeId=" + tradeId +
                ", userId=" + userId +
                ", qty=" + qty +
                ", tradeQty=" + tradeQty +
                ", createTime=" + createTime +
                '}';
    }
}
