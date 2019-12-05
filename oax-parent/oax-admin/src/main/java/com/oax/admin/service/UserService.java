package com.oax.admin.service;

import com.github.pagehelper.PageInfo;
import com.oax.entity.admin.User;
import com.oax.entity.admin.param.UserPageParam;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/5/29
 * Time: 18:09
 */
public interface UserService {
    User selectByUsername(String username);

    /**
     * 分页获取 user
     *
     * @param userPageParam
     * @return
     */
    PageInfo<User> selectByPage(UserPageParam userPageParam);

    int insert(User user);

    int delUser(Integer id);

    int update(User shiroUser);

    User selectById(Integer adminId);
}
