package com.oax.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.service.RoleService;
import com.oax.entity.admin.Role;
import com.oax.mapper.admin.RoleMapper;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/5/31
 * Time: 10:34
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public PageInfo<Role> selectByPage(int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<Role> roleList = roleMapper.selectAll();
        return new PageInfo<>(roleList);
    }

    @Override
    public List<Role> selectRoleListWithSelected(int userId) {
        return roleMapper.selectRoleListWithSelected(userId);
    }

    @Override
    public int insert(Role role) {
        return roleMapper.insert(role);
    }

    @Override
    public int delRole(int roleId) {
        return roleMapper.deleteByPrimaryKey(roleId);
    }
}
