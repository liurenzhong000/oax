package com.oax.entity.enums;


import com.baomidou.mybatisplus.core.enums.IEnum;

public enum CtcAdvertStatus implements IEnum<Integer> {

    PUTAWAY("发布"), SOLDOUT("下架");

    public String desc;

    CtcAdvertStatus(String desc) {
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.ordinal();
    }
}
