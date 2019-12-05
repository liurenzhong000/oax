package com.oax.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TradeDetail{
    private String id;
    private BigDecimal amount;
    private BigDecimal price;
    private String direction;
    private String ts;
}
