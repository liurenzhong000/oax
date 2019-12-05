package com.oax.entity.front;

import java.math.BigDecimal;

public class RedPacketLimitInfo extends RedPacketLimit {

    private BigDecimal cnyPrice;

    public BigDecimal getCnyPrice() {
        return cnyPrice;
    }

    public void setCnyPrice(BigDecimal cnyPrice) {
        this.cnyPrice = cnyPrice;
    }


}
