package com.oax.domain;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class TradeData<T> {

    private String id;
    private String ts;
    private List<TradeDetail> data;
}



