package com.oax.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oax.admin.service.RoleResourcesService;
import com.oax.admin.service.UserRoleService;
import com.oax.admin.shiro.MyShiroRealm;
import com.oax.entity.admin.RoleResourcesKey;
import com.oax.mapper.admin.RoleResourcesMapper;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/5/29
 * Time: 18:39
 */
@Service
public class RoleResourcesServiceImpl implements RoleResourcesService {

    @Autowired
    private RoleResourcesMapper roleResourcesMapper;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private MyShiroRealm myShiroRealm;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addRoleResources(RoleResourcesKey roleResources) {

        int delete = roleResourcesMapper.deleteByRoleId(roleResources.getRoleid());
        String resourcesidArray = roleResources.getResourcesidArray();
        String[] resourcesArr = resourcesidArray.split(",");
        for (String resourcesId : resourcesArr) {
            RoleResourcesKey r = new RoleResourcesKey();
            r.setRoleid(roleResources.getRoleid());
            r.setResourcesid(Integer.parseInt(resourcesId));
            delete = roleResourcesMapper.insert(r);
        }

        List<Integer> userIds = userRoleService.findUserIdByRoleId(roleResources.getRoleid());
        //更新当前登录的用户的权限缓存
        myShiroRealm.clearUserAuthByUserId(userIds);
        return delete;
    }
}
