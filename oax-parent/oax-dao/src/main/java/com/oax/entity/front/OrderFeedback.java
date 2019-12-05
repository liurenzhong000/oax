package com.oax.entity.front;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OrderFeedback implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer orderId;
    private Integer marketId;
    private Integer userId;
    private BigDecimal qty;
    private BigDecimal orderQty;
    private Date createTime;

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

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(BigDecimal orderQty) {
        this.orderQty = orderQty;
    }

    @Override
    public String toString() {
        return "OrderFeedback{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", marketId=" + marketId +
                ", userId=" + userId +
                ", qty=" + qty +
                ", orderQty=" + orderQty +
                ", createTime=" + createTime +
                '}';
    }
}
