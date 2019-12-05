package com.oax.admin.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.oax.entity.admin.Role;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/5/31
 * Time: 10:34
 */
public interface RoleService {
    /**
     * 分页查询角色
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageInfo<Role> selectByPage(int pageNo, int pageSize);

    /**
     * 查询所有 角色 -> 选中对应用户的角色
     *
     * @param userId
     * @return
     */
    List<Role> selectRoleListWithSelected(int userId);

    int insert(Role role);

    int delRole(int roleId);
}
