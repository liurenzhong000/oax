package com.oax.entity.admin.vo;

import java.util.List;

import org.springframework.data.annotation.Transient;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/13
 * Time: 18:18
 * admin 菜单展示
 */
@Data
public class MenuVo {

    /**
     * 菜单资源id
     */
    private int id;

    /**
     * 菜单 父节点pid
     */
    private int parentid;

    /**
     * 资源
     */
    private String resurl;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 资源类型
     */
    private Integer type;

    /**
     * 子资源
     */
    private List<MenuVo> childrens;

    /**
     * 是否选中
     */
    @Transient
    private boolean checked;
}
