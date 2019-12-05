package com.oax.entity.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum UserCoinDetailType implements IEnum<Integer> {
    RECHARGE("充值"), WITHDRAW("提现"), WITHDRAW_FAIL("提现拒绝"), SELL_ENTRUST("卖出委托"), BUY_ENTRUST("买入委托"), MATCH("订单撮合"),
    CTC_ORDER("C2C用户下单"), CTC_CANCEL("C2C取消订单"), CTC_FINISH("C2C完成订单"), CTC_CLOSE("C2C订单关闭"), BONUS("BHB分红"),
    PANIC_BUY_BHB("抢购"), DICE("DICE"), SIGN_IN("签到"), ID_CHECK("身份证认证"), DICE_BCB_MINING("dice挖矿获得BCB"),
    SNATCH("一元夺宝"), SNATCH_BCB_MINING("snatch挖矿获得BCB"), BLAST_POINT("爆点"), BCB_BONUS("BCB分红");
    public String desc;

    UserCoinDetailType(String desc) {
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.ordinal();
    }
}
