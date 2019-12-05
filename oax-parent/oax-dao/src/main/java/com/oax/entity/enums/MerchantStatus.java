package com.oax.entity.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum MerchantStatus implements IEnum<Integer> {

    NORMAL("正常"), DELETE("删除");

    public String desc;

    MerchantStatus(String desc) {
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.ordinal();
    }
}
