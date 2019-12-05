package com.oax.common.enums;

import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/11
 * Time: 11:09
 * 转出记录 状态
 * <p>
 * <p>
 * <p>
 * 状态
 * -3:转出失败
 * -2:终审不通过
 * -1：初审不通过
 * 0：待初审
 * 1:待终审
 * 2：已转出(已产生交易hash)
 * 3：已广播（离开txpool）
 * 4：已确认
 */
@Getter
public enum WithdrawStatusEnum {

    /**
     * 待初审
     */
    WAIT_FIRST_CHECK((byte) 0),

    /**
     * 待终审
     */
    WAIT_LAST_CHECK((byte) 1),

    /**
     * 初审失败
     */
    FIRST_CHECK_FAIL((byte) -1),

    /**
     * 终审失败
     */
    LAST_CHECK_FAIL((byte) -2),

    /**
     * 转出失败
     */
    FALL_STATUS((byte) -3),

    /**
     * 已转出(已产生交易hash)
     */
    IN_TXPOOL_STATUS((byte) 2),

    /**
     * 已广播（离开txpool）
     */
    OUT_TXPOOL_STATUS((byte) 3),

    /**
     * 已确认
     */
    CONFIRM_STATUS((byte) 4),

    /**
     * 已拉黑
     */
    BLOCK((byte) 5);

    private byte status;

    WithdrawStatusEnum(byte status) {
        this.status = status;
    }
}
