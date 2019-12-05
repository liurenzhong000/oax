package com.oax.admin.enums;

import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/19
 * Time: 11:32
 */
@Getter
public enum RechargeTypeEnum {

    /**
     * 用户转入
     */
    RECHARGE(0),
    /**
     * 福利
     */
    WELFARES(1),

    /**
     * 其他
     */
    ORTHER(3)

    ;

    private int type;

    RechargeTypeEnum(int type) {
        this.type = type;
    }
}
