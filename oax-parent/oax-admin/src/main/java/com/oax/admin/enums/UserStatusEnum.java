package com.oax.admin.enums;

import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/1
 * Time: 17:01
 * 用户状态枚举类
 */
@Getter
public enum UserStatusEnum {

    /**
     * 启用
     */
    ENABLE(1),
    /**
     * 禁用
     */
    UNABLE(0),;

    private int status;

    UserStatusEnum(int status) {
        this.status = status;
    }


}
