package com.oax.common.enums;

import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/11
 * Time: 11:09
 * 用户状态 状态
 * <p>
 * <p>
 * <p>
 * lv2审核状态 ：
 * -1未通过
 * 0未认证
 * 1待审核
 * 2审核通过
 */
@Getter
public enum UserStatusEnum {

    /**
     * -1未通过
     */
    NOT_PASS(-1),

    /**
     * 0未认证
     */
    NOT_AUTHENTICATION(0),

    /**
     * 1待审核
     */
    WAIT_VERIFY(1),

    /**
     * 2审核通过
     */
    PASS_VERIFY(2),

    /**
     * 未激活
     */
    LEVEL_0(0),

    LEVEL_1(1),

    LEVEL_2(2),

    LEVEL_3(3)

   ;

    private int status;

    UserStatusEnum(int status) {
        this.status = status;
    }
}
