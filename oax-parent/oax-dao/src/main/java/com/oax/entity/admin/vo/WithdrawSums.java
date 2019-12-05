package com.oax.entity.admin.vo;

import java.math.BigDecimal;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/11
 * Time: 16:19
 * 虚拟币转出 页面总数展示
 */
@Data
public class WithdrawSums {

    private BigDecimal countQty;

    private BigDecimal countFee;

    private BigDecimal countAccount;


    public BigDecimal getCountAccount() {
        if (countQty != null && countFee != null) {
            return countQty.subtract(countFee);
        }
        return null;
    }

}
