package com.oax.entity.front;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * MarketInformation
 *
 * @author
 */
public class MarketInformation implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private BigDecimal low;
    private BigDecimal high;
    private BigDecimal last;
    private BigDecimal sell;
    private BigDecimal buy;
    private BigDecimal volume;
    private BigDecimal turnover;
    private BigDecimal changeRate;

    public Integer getId() {
        return id;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLast() {
        return last;
    }

    public void setLast(BigDecimal last) {
        this.last = last;
    }

    public BigDecimal getSell() {
        return sell;
    }

    public void setSell(BigDecimal sell) {
        this.sell = sell;
    }

    public BigDecimal getBuy() {
        return buy;
    }

    public void setBuy(BigDecimal buy) {
        this.buy = buy;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getTurnover() {
        return turnover;
    }

    public void setTurnover(BigDecimal turnover) {
        this.turnover = turnover;
    }

    public BigDecimal getChangeRate() {
        return changeRate;
    }

    public void setChangeRate(BigDecimal changeRate) {
        this.changeRate = changeRate;
    }

}