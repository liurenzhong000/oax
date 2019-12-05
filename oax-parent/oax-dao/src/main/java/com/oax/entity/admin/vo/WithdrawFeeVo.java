package com.oax.entity.admin.vo;

import java.math.BigDecimal;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/23
 * Time: 17:39
 * 转出手续费 展示
 */
@Data
public class WithdrawFeeVo {

    private int coinId;

    private String coinName;

    private BigDecimal fee;
}
