package com.oax.admin.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oax.admin.service.UserRoleService;
import com.oax.admin.shiro.MyShiroRealm;
import com.oax.entity.admin.UserRole;
import com.oax.mapper.admin.UserRoleMapper;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/5/29
 * Time: 18:38
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private MyShiroRealm myShiroRealm;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertUserRole(UserRole userRole) {


        int i = userRoleMapper.delByUserId(userRole.getUserid());

        String roleidArray = userRole.getRoleidArray();
        String[] roleids = roleidArray.split(",");
        for (String roleId : roleids) {
            UserRole u = new UserRole();
            u.setUserid(userRole.getUserid());
            u.setRoleid(Integer.parseInt(roleId));
            userRoleMapper.insert(u);
        }
        //更新当前登录的用户的权限缓存
        List<Integer> userid = new ArrayList<Integer>();
        userid.add(userRole.getUserid());
        myShiroRealm.clearUserAuthByUserId(userid);
        return i;
    }

    @Override
    public List<Integer> findUserIdByRoleId(Integer roleid) {
        return userRoleMapper.selectUserIdByRoleId(roleid);
    }
}
