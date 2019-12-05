package com.oax.entity.admin.param;

import javax.validation.constraints.NotNull;

/*
 *  用户锁定按钮
 */
public class UserLockStatusParam {
    // 未锁定状态
    public static final Integer LOCK_STATUS_ZERO = 0;
    // 锁定状态
    public static final Integer LOCK_STATUS_ONE = 1;
    //用户id
    @NotNull(message = "用户id不能为空")
    private Integer userId;
    //用户是否锁定状态  0 解锁 1锁定
    @NotNull(message = "是否锁定不能为空")
    private Integer lockStatus;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(Integer lockStatus) {
        this.lockStatus = lockStatus;
    }
}
