package com.oax.entity.front;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * TradeRecord
 *
 * @author
 */
public class TradeRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private BigDecimal amount;

    private BigDecimal qty;

    private BigDecimal price;

    private String type;

    private String createTime;

    private Long createTimeMs;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getCreateTimeMs() {
        return createTimeMs;
    }

    public void setCreateTimeMs(Long createTimeMs) {
        this.createTimeMs = createTimeMs;
    }
}