package com.oax.entity.front.vo;

import com.oax.entity.enums.CtcOrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * CTO 用户购买返回数据
 */
@Setter
@Getter
public class CtcBuyVo {

    /**开户用户名称*/
    private String realName;

    /**开户银行名称*/
    private String bankName;

    /**银行卡号*/
    private String cardNo;

    /**交易总价*/
    private BigDecimal totalCost;

    /**订单id*/
    private Long ctcOrderId;

    private CtcOrderStatus status;

    private String statusDesc;

}
