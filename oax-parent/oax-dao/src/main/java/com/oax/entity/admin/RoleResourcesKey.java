/*
 *
 * RoleResourcesKey.java
 * Copyright(C) 2018 oax公司
 * @date 2018-05-29
 */
package com.oax.entity.admin;

import java.io.Serializable;

public class RoleResourcesKey implements Serializable {
    /**
     *
     */
    private Integer roleid;

    /**
     *
     */
    private Integer resourcesid;

    /**
     * 多个资源id 用逗号","隔开
     */
    private String resourcesidArray;

    public String getResourcesidArray() {
        return resourcesidArray;
    }

    public void setResourcesidArray(String resourcesidArray) {
        this.resourcesidArray = resourcesidArray;
    }

    /**
     * @return roleId
     */
    public Integer getRoleid() {
        return roleid;
    }

    /**
     * @param roleid
     */
    public void setRoleid(Integer roleid) {
        this.roleid = roleid;
    }

    /**
     * @return resourcesId
     */
    public Integer getResourcesid() {
        return resourcesid;
    }

    /**
     * @param resourcesid
     */
    public void setResourcesid(Integer resourcesid) {
        this.resourcesid = resourcesid;
    }
}