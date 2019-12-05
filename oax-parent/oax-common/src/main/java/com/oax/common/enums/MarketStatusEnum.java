package com.oax.common.enums;

import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/6
 * Time: 20:05
 * 交易对 状态
 * 0不展示 1展示
 */
@Getter
public enum MarketStatusEnum {

    SHOW((byte) 1),
    CLOSE((byte) 0),

    AUTO_ADD((byte)1),
    NOT_AUTO_ADD((byte)0),

    AUTO_MINE((byte)1),

    NOT_AUTO_MINE((byte)0)

    ;

    private byte status;

    MarketStatusEnum(byte status) {
        this.status = status;
    }
}
