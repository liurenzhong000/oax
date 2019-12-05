/*
 *
 * UserRoleMapper.java
 * Copyright(C) 2018 oax公司
 * @date 2018-05-29
 */
package com.oax.mapper.admin;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.oax.entity.admin.UserRole;

@Mapper
public interface UserRoleMapper {
    /**
     * @mbg.generated 2018-05-29
     */
    int insert(UserRole record);

    /**
     * @mbg.generated 2018-05-29
     */
    int insertSelective(UserRole record);

    /**
     * 根据userid删除用户角色
     *
     * @param userid 用户id
     * @return
     */
    int delByUserId(Integer userid);

    List<Integer> selectUserIdByRoleId(Integer roleid);
}