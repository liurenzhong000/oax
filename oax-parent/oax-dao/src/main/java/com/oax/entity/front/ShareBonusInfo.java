package com.oax.entity.front;

import java.math.BigDecimal;

public class ShareBonusInfo {
    private static final long serialVersionUID = 1L;
    private Integer coinId;
    private String coinName;
    private BigDecimal avgQty;
    private BigDecimal toDayQty;
    private BigDecimal FridayQty;
    private BigDecimal MondayQty;

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

    public BigDecimal getAvgQty() {
        return avgQty;
    }

    public void setAvgQty(BigDecimal avgQty) {
        this.avgQty = avgQty;
    }

    public BigDecimal getToDayQty() {
        return toDayQty;
    }

    public void setToDayQty(BigDecimal toDayQty) {
        this.toDayQty = toDayQty;
    }

    public BigDecimal getFridayQty() {
        return FridayQty;
    }

    public void setFridayQty(BigDecimal fridayQty) {
        FridayQty = fridayQty;
    }

    public BigDecimal getMondayQty() {
        return MondayQty;
    }

    public void setMondayQty(BigDecimal mondayQty) {
        MondayQty = mondayQty;
    }

    @Override
    public String toString() {
        return "ShareBonusInfo{" +
                "coinId=" + coinId +
                ", coinName='" + coinName + '\'' +
                ", avgQty=" + avgQty +
                ", toDayQty=" + toDayQty +
                ", FridayQty=" + FridayQty +
                ", MondayQty=" + MondayQty +
                '}';
    }
}
