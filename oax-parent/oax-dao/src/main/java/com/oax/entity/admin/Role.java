/*
 *
 * Role.java
 * Copyright(C) 2018 oax公司
 * @date 2018-05-29
 */
package com.oax.entity.admin;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;

public class Role implements Serializable {
    /**
     *
     */
    private Integer id;

    /**
     *
     */
    @NotEmpty(message = "角色名不能为空")
    private String roledesc;
    @Transient
    private Integer selected;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return roleDesc
     */
    public String getRoledesc() {
        return roledesc;
    }

    /**
     * @param roledesc
     */
    public void setRoledesc(String roledesc) {
        this.roledesc = roledesc == null ? null : roledesc.trim();
    }

    public Integer getSelected() {
        return selected;
    }

    public void setSelected(Integer selected) {
        this.selected = selected;
    }
}