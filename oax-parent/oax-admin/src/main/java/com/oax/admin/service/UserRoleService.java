package com.oax.admin.service;

import java.util.List;

import com.oax.entity.admin.UserRole;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/5/29
 * Time: 18:38
 */
public interface UserRoleService {
    int insertUserRole(UserRole userRole);


    List<Integer> findUserIdByRoleId(Integer roleid);
}
