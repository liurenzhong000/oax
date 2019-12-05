package com.oax.entity.front.vo;

import com.oax.entity.enums.CtcOrderStatus;
import com.oax.entity.enums.CtcOrderType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * CTO 用户购买返回数据
 */
@Setter
@Getter
public class CtcOrderDetailVo {

    /**开户用户名称*/
    private String realName;

    /**开户银行名称*/
    private String bankName;

    /**银行开户支行*/
    private String bankBranch;

    /**银行卡号*/
    private String cardNo;

    /**交易总价*/
    private BigDecimal totalCost;

    /**订单id*/
    private Long ctcOrderId;

    /**订单状态*/
    private CtcOrderStatus status;

    /**订单状态描述*/
    private String statusDesc;

    private CtcOrderType type;

    /**
     * 交易时的币种单价
     */
    private BigDecimal price;

    /**
     * 数量
     */
    private BigDecimal qty;

}
