package com.oax.entity.admin.vo;

import java.math.BigDecimal;

public class UserDetailsVo {
    //币名称 币种
    private String shortName;
    //余额 (总)
    private BigDecimal allBanlance;
    //可用余额
    private BigDecimal banlance;
    //冻结
    private BigDecimal freezingBanlance;
    //总转入
    private BigDecimal sumRechargeQty;
    //总转出
    private BigDecimal sumWithdrawQty;
    //总转出手续费
    private BigDecimal sumWithdrawFee;
    //总交易手续费
    private BigDecimal sumTradeFee;

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public BigDecimal getAllBanlance() {
        return allBanlance;
    }

    public void setAllBanlance(BigDecimal allBanlance) {
        this.allBanlance = allBanlance;
    }

    public BigDecimal getBanlance() {
        return banlance;
    }

    public void setBanlance(BigDecimal banlance) {
        this.banlance = banlance;
    }

    public BigDecimal getFreezingBanlance() {
        return freezingBanlance;
    }

    public void setFreezingBanlance(BigDecimal freezingBanlance) {
        this.freezingBanlance = freezingBanlance;
    }

    public BigDecimal getSumRechargeQty() {
        return sumRechargeQty;
    }

    public void setSumRechargeQty(BigDecimal sumRechargeQty) {
        this.sumRechargeQty = sumRechargeQty;
    }

    public BigDecimal getSumWithdrawQty() {
        return sumWithdrawQty;
    }

    public void setSumWithdrawQty(BigDecimal sumWithdrawQty) {
        this.sumWithdrawQty = sumWithdrawQty;
    }

    public BigDecimal getSumWithdrawFee() {
        return sumWithdrawFee;
    }

    public void setSumWithdrawFee(BigDecimal sumWithdrawFee) {
        this.sumWithdrawFee = sumWithdrawFee;
    }

    public BigDecimal getSumTradeFee() {
        return sumTradeFee;
    }

    public void setSumTradeFee(BigDecimal sumTradeFee) {
        this.sumTradeFee = sumTradeFee;
    }
}
