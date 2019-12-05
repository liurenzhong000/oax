package com.oax.common.enums;

import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/21
 * Time: 17:47
 * 平台转账 类型
 * 1：申请手续费 2：提币到主地址 3：转到冷钱包
 */
@Getter
public enum PlatformTransferTypeEnum {

    /**
     * 申请手续费
     */
    FEE_TYPE(1),
    /**
     * 提币到主地址
     */
    TO_MAIN_ADDRESS_TYPE(2),
    /**
     * 转到冷钱包
     */
    TO_COLD_ADDRESS_TYPE(3),

    /**
     * 状态
     * 0：失败
     * 1：已产生交易Hash，并进入txPool
     * 2：离开txPool
     * 3：已确认
     */

    /**
     * 0：失败
     */
    FALL_STATUS(0),

    /**
     * 1：已产生交易Hash，并进入txPool
     */
    IN_TXPOOL_STATUS(1),

    /**
     * 2：离开txPool
     */
    OUT_TXPOOL_STATUS(2),

    /**
     * 3：已确认
     */
    CONFIRM_STATUS(3);
    private int type;

    PlatformTransferTypeEnum(int type) {
        this.type = type;
    }

}
