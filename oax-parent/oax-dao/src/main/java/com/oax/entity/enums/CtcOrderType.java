package com.oax.entity.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * ctc订单类型，买入卖出是相对用户来说的
 * 如果相对商户来说，SALE=买入，BUY=卖出
 */
public enum CtcOrderType implements IEnum<Integer> {

    USER_BUY("买入","卖出"), USER_SALE("卖出","买入");

    public String descForUser;

    public String descForMerchant;

    CtcOrderType(String descForUser, String descForMerchant) {
        this.descForUser = descForUser;
        this.descForMerchant = descForMerchant;
    }

    @Override
    public Integer getValue() {
        return this.ordinal();
    }
}