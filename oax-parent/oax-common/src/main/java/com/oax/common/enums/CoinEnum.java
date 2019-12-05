package com.oax.common.enums;

import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/14
 * Time: 18:31
 * rediskey 枚举类
 */
@Getter
public enum CoinEnum {

    //币种id
    TOKEN_ETH(1),
    TOKEN_BTC(2),
    TOKEN_X(3),
    TOKEN_USDT(10);
    private Integer value;

    CoinEnum(Integer value) {
        this.value = value;
    }
}
