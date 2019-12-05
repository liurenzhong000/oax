/*
 *
 * RoleResourcesMapper.java
 * Copyright(C) 2018 oax公司
 * @date 2018-05-29
 */
package com.oax.mapper.admin;

import org.apache.ibatis.annotations.Mapper;

import com.oax.entity.admin.RoleResourcesKey;

@Mapper
public interface RoleResourcesMapper {
    /**
     * @mbg.generated 2018-05-29
     */
    int deleteByPrimaryKey(RoleResourcesKey key);

    /**
     * @mbg.generated 2018-05-29
     */
    int insert(RoleResourcesKey record);

    /**
     * @mbg.generated 2018-05-29
     */
    int insertSelective(RoleResourcesKey record);

    int deleteByRoleId(Integer roleid);
}