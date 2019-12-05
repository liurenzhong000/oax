package com.oax.entity.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum CtcOrderLogType implements IEnum<Integer> {
    NO_PAY("用户下单"), PAY("点击付款"), CANCEL("取消订单"), APPEAL("用户申诉"), FINISH("放币，交易完成"),
    CANCEL_APPEAL("用户撤销申诉"), PROCESSING("平台处理"), PLATFORM_FINISH("平台操作放币，完成交易"),
    CLOSE("平台关闭订单"), TIME_OUT("订单超时"), CLOSE_APPEAL("平台关闭申诉");

    public String desc;

    CtcOrderLogType(String desc) {
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.ordinal();
    }
}
