package com.oax.entity.enums;


import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * 卖出，买入是相对商家来说的
 */
public enum CtcAdvertType implements IEnum<Integer> {

    SALE("卖出"), BUY("买入");

    public String desc;

    CtcAdvertType(String desc) {
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.ordinal();
    }
}
