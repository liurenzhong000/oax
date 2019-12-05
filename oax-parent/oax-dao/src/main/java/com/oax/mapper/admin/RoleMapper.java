/*
 *
 * RoleMapper.java
 * Copyright(C) 2018 oax公司
 * @date 2018-05-29
 */
package com.oax.mapper.admin;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.oax.entity.admin.Role;

@Mapper
public interface RoleMapper {
    /**
     * @mbg.generated 2018-05-29
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * @mbg.generated 2018-05-29
     */
    int insert(Role record);

    /**
     * @mbg.generated 2018-05-29
     */
    int insertSelective(Role record);

    /**
     * @mbg.generated 2018-05-29
     */
    Role selectByPrimaryKey(Integer id);

    /**
     * @mbg.generated 2018-05-29
     */
    int updateByPrimaryKeySelective(Role record);

    /**
     * @mbg.generated 2018-05-29
     */
    int updateByPrimaryKey(Role record);

    /**
     * 查询所有 权限
     *
     * @return
     */
    List<Role> selectAll();

    /**
     * 查询所有 角色 -> 选中对应用户的角色
     *
     * @param userId
     * @return
     */
    List<Role> selectRoleListWithSelected(int userId);
}