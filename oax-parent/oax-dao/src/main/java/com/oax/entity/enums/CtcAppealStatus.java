package com.oax.entity.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum CtcAppealStatus implements IEnum<Integer> {

    APPEAL("申诉中"), DEALING("申诉处理中"), FINISH("处理完成"), CANCEL("已撤销"), CLOSE("关闭订单"), CLOSE_APPEAL("关闭申诉");

    public String desc;

    CtcAppealStatus(String desc) {
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.ordinal();
    }
}
