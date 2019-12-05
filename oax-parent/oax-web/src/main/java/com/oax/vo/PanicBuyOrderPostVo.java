package com.oax.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PanicBuyOrderPostVo {

    /**活动id*/
    private Integer activityId;

    /**用户id*/
    private Integer userId;
    /**购买个数*/
    private BigDecimal qty;

    /**当前bhb/usdt价格*/
    private BigDecimal currPrice;

    /**总usdt个数*/
    private BigDecimal allUsdtQty;

    /**买入bhb/usdt价格*/
    private BigDecimal buyPrice;

    /**真实usdt个数*/
    private BigDecimal realUsdtQty;

    /**用户usdt余额*/
    private BigDecimal banlance;

    /**是否有交易密码*/
    private boolean hasTransactionPassword;

    /**生成的订单id*/
    private Integer orderId;
}
