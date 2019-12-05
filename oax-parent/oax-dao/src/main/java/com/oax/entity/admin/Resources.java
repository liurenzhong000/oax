/*
 *
 * Resources.java
 * Copyright(C) 2018 oax公司
 * @date 2018-05-29
 */
package com.oax.entity.admin;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Transient;

public class Resources implements Serializable {
    /**
     *
     */
    private Integer id;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 资源url
     */
    private String resurl;

    /**
     * 资源类型   1:菜单    2：按钮
     */
    private Integer type;

    /**
     * 父资源
     */
    private Integer parentid;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否选中
     */
    @Transient
    private String checked;

    /**
     * url 方式
     */
    private String method;

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
     * 资源名称
     *
     * @return name 资源名称
     */
    public String getName() {
        return name;
    }

    /**
     * 资源名称
     *
     * @param name 资源名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 资源url
     *
     * @return resUrl 资源url
     */
    public String getResurl() {
        return resurl;
    }

    /**
     * 资源url
     *
     * @param resurl 资源url
     */
    public void setResurl(String resurl) {
        this.resurl = resurl == null ? null : resurl.trim();
    }

    /**
     * 资源类型   1:菜单    2：按钮
     *
     * @return type 资源类型   1:菜单    2：按钮
     */
    public Integer getType() {
        return type;
    }

    /**
     * 资源类型   1:菜单    2：按钮
     *
     * @param type 资源类型   1:菜单    2：按钮
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 父资源
     *
     * @return parentId 父资源
     */
    public Integer getParentid() {
        return parentid;
    }

    /**
     * 父资源
     *
     * @param parentid 父资源
     */
    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    /**
     * 排序
     *
     * @return sort 排序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 排序
     *
     * @param sort 排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String toFilterChain() {

        return "myPerms" + "[" + getUrl() + "]";
    }

    public String getUrl() {

        if (StringUtils.isEmpty(getMethod())) {
            return getResurl();
        }
        return getResurl() + "==" + getMethod();
    }
}