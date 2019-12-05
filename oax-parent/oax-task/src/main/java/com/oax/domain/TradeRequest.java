package com.oax.domain;

import lombok.Data;

@Data
public class TradeRequest {

    //交易对
    public String symbol;

    /**size [1, 2000]*/
    public Integer size;
}
