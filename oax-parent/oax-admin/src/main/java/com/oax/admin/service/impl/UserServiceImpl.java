package com.oax.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.service.UserService;
import com.oax.entity.admin.User;
import com.oax.entity.admin.param.UserPageParam;
import com.oax.mapper.admin.UserMapper;
import com.oax.mapper.admin.UserRoleMapper;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/5/29
 * Time: 18:09
 * 管理员 service层
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public User selectByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public PageInfo<User> selectByPage(UserPageParam userPageParam) {
        PageHelper.startPage(userPageParam.getPageNum(), userPageParam.getPageSize());
        List<User> users = userMapper.selectAllByUserPageParam(userPageParam);
        return new PageInfo<>(users);
    }

    @Override
    public int insert(User user) {
        return userMapper.insert(user);
    }

    @Override
    public int delUser(Integer id) {
        //删除用户表
        userMapper.deleteByPrimaryKey(id);
        //删除用户角色表

        userRoleMapper.delByUserId(id);
        return 0;
    }

    @Override
    public int update(User shiroUser) {
        return userMapper.updateByPrimaryKeySelective(shiroUser);
    }

    @Override
    public User selectById(Integer adminId) {
        return userMapper.selectByPrimaryKey(adminId);
    }
}
