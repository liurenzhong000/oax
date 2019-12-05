package com.oax.form;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class UpdateCtcAdvertForm {
    /**
     * 最小下单个数（最小买入数量/ 最小卖出数量）
     */
    @NonNull
    private BigDecimal minQty;

    /**
     * 最大下单个数（最大买入数量/ 最大卖出数量）
     */
    @NonNull
    private BigDecimal maxQty;

    /**
     * 人民币单价
     */
    @NonNull
    private BigDecimal cnyPrice;

    /**
     * 剩余可交易数量
     */
    @NonNull
    private BigDecimal remainQty;

}
