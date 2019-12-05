package com.oax.entity.admin.param;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/12
 * Time: 15:42
 * 权限管理 条件查询用户列表
 */
@Data
public class UserPageParam extends PageParam {

    private Integer userId;

    private String username;

    /**
     * 用户状态 是否启用 1启用 0 禁用
     */
    private Integer userStatus;

}
