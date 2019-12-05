package com.oax.entity.front.vo;

import com.oax.entity.enums.CtcOrderStatus;
import com.oax.entity.enums.CtcOrderType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * CTC 订单，用户交易记录
 */
@Setter
@Getter
public class CtcOrderForUserVo {

    /**ctc订单id*/
    private Long id;

    /**买入数量*/
    private BigDecimal qty;

    /**买入总金额*/
    private BigDecimal totalCost;

    /**单价*/
    private BigDecimal price;

    /**类型*/
    private CtcOrderType type;

    /**类型描述*/
    private String typeDesc;

    /**状态*/
    private CtcOrderStatus status;

    /**状态描述*/
    private String statusDesc;

    /**买入时间*/
    private Date createDate;

    /**到账时间*/
    private Date finishDate;

    /**申诉状态时是否为申诉人*/
    private boolean appealed;
}
