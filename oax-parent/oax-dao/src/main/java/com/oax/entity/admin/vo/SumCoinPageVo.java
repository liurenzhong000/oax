package com.oax.entity.admin.vo;

import java.math.BigDecimal;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/12
 * Time: 14:28
 * 平台虚拟币统计展示
 */
@Data
public class SumCoinPageVo {

    private int coinId;

    /**
     * 币种名称
     */
    private String coinName;

    /**
     * 用户余额
     */
    private BigDecimal userBalance;

    /**
     * 用户总转入
     */
    private BigDecimal rechargeQty;

    /**
     * 用户总转出
     */
    private BigDecimal withdrawQty;

    /**
     * 用户总转出手续费
     */
    private BigDecimal withdrawFee;

    /**
     * 交易手续费
     */
    private BigDecimal tradeFee;

    /**
     * 平台成本
     */
    private BigDecimal platformCost;

}
