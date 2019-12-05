package com.oax.common.enums;

import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/6
 * Time: 10:48
 * 币种 类型
 */
@Getter
public enum CoinTypeEnum {


    ETH(1),
    BTC(2),
    ETH_TOKEN(3),
    USDT(4),;

    private int type;

    CoinTypeEnum(int type) {
        this.type = type;
    }

}
