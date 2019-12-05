package com.oax.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 用户资金页
 * 用户总资金数据
 */
@Data
public class UserCoinTotalVo {

    private BigDecimal btcPrice;

    private BigDecimal cnyPrice;

    private BigDecimal ethPrice;

    private BigDecimal usdtPrice;
}
