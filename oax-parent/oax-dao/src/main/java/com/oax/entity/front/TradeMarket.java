package com.oax.entity.front;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * market
 *
 * @author
 */
public class TradeMarket implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private BigDecimal newPrice;
    private BigDecimal cnyPrice;
    private BigDecimal rate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(BigDecimal newPrice) {
        this.newPrice = newPrice;
    }

    public BigDecimal getCnyPrice() {
        return cnyPrice;
    }

    public void setCnyPrice(BigDecimal cnyPrice) {
        this.cnyPrice = cnyPrice;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

}