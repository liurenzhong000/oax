package com.oax.entity.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum PaymentWay implements IEnum<Integer> {

    BANK_CARD("银行卡");

    public String desc;

    PaymentWay(String desc) {
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.ordinal();
    }
}
