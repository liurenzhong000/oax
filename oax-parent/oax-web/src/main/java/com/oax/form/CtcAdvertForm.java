package com.oax.form;

import com.oax.entity.enums.CtcAdvertType;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class CtcAdvertForm {

    /**
     * 交易币种类型
     */
    private Integer coinId;

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
     * 留言信息
     */
    private String leaveMessage;

    /**
     * 人民币单价
     */
    @NonNull
    private BigDecimal cnyPrice;

    /**
     * 总交易数量
     */
    @NonNull
    private BigDecimal totalQty;

    @NonNull
    private CtcAdvertType type;

}
