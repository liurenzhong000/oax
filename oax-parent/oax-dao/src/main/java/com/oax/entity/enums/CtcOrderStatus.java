package com.oax.entity.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum  CtcOrderStatus implements IEnum<Integer> {

    NO_PAY("未付款"), PAYED("已付款"), APPEAL("申诉中"), PROCESSING("申诉处理中"), FINISH("交易完成"), CANCEL("已取消");

    public String desc;

    CtcOrderStatus(String desc) {
        this.desc = desc;
    }
    @Override
    public Integer getValue() {
        return this.ordinal();
    }
}
