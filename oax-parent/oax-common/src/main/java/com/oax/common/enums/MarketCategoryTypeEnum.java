package com.oax.common.enums;

import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/6
 * Time: 17:30
 * 市场分区type枚举类
 */
@Getter
public enum MarketCategoryTypeEnum {

    USED((byte) 1),
    CLOSE((byte) 0),;

    private byte type;

    MarketCategoryTypeEnum(byte type) {
        this.type = type;
    }
}

