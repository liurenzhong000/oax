/*
 *
 * UserRole.java
 * Copyright(C) 2018 oax公司
 * @date 2018-05-29
 */
package com.oax.entity.admin;

import java.io.Serializable;

public class UserRole implements Serializable {
    /**
     *
     */
    private Integer userid;

    /**
     *
     */
    private Integer roleid;

    /**
     * 角色id 数组
     */
    private String roleidArray;

    public String getRoleidArray() {
        return roleidArray;
    }

    public void setRoleidArray(String roleidArray) {
        this.roleidArray = roleidArray;
    }

    /**
     * @return userId
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * @param userid
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
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
}