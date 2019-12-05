package com.oax.admin.enums;

import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/25
 * Time: 21:46
 * 操作员记录表
 *
 * 1查看用户详情中资产
 * 2查看用户详情中转入记录
 * 3查询转入记录列表
 */
@Getter
public enum  OperationLogEnum {


    /**
     * 1查看用户详情中资产
     */
    USER_DETAIL_ASSET(1),
    /**
     * 2查看用户详情中转入记录
     */
    USER_DETAIL_RECHARGE(2),
    /**
     * 3查询转入记录列表
     */
    RECHARGE(3)
    ;

    private int type;

    OperationLogEnum(int type) {
        this.type = type;
    }
}
