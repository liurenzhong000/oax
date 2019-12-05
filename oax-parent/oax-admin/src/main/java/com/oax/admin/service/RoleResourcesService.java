package com.oax.admin.service;

import com.oax.entity.admin.RoleResourcesKey;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/5/29
 * Time: 18:37
 */
public interface RoleResourcesService {


    /**
     * 给角色添加资源
     *
     * @param roleResources
     * @return
     */
    int addRoleResources(RoleResourcesKey roleResources);
}
