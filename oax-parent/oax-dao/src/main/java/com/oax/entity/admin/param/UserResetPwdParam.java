package com.oax.entity.admin.param;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/6
 * Time: 15:30
 */
@Data
public class UserResetPwdParam {

    @NotEmpty(message = "旧密码不能为空")
    private String oldPwd;

    @NotEmpty(message = "新密码不能为空")
    private String newPwd;
}
