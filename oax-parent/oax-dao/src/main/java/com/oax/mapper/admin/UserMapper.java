/*
 *
 * UserMapper.java
 * Copyright(C) 2018 oax公司
 * @date 2018-05-29
 */
package com.oax.mapper.admin;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.oax.entity.admin.User;
import com.oax.entity.admin.param.UserPageParam;

@Mapper
public interface UserMapper {
    /**
     * @mbg.generated 2018-05-29
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * @mbg.generated 2018-05-29
     */
    int insert(User record);

    /**
     * @mbg.generated 2018-05-29
     */
    int insertSelective(User record);

    /**
     * @mbg.generated 2018-05-29
     */
    User selectByPrimaryKey(Integer id);

    /**
     * @mbg.generated 2018-05-29
     */
    int updateByPrimaryKeySelective(User record);

    /**
     * @mbg.generated 2018-05-29
     */
    int updateByPrimaryKey(User record);

    /**
     * 通过username查询用户
     *
     * @param username
     * @return
     */
    User selectByUsername(String username);

    /**
     * 获取所有user
     *
     * @return
     */
    List<User> selectAll();

    /**
     * 根据页码参数获取 用户
     *
     * @param userPageParam
     * @return
     */
    List<User> selectAllByUserPageParam(UserPageParam userPageParam);
}