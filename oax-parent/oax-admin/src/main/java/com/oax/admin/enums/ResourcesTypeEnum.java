package com.oax.admin.enums;

import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/1
 * Time: 17:12
 * 资源类型枚举类
 */
@Getter
public enum ResourcesTypeEnum {

    /**
     * 菜单
     */
    MENU(1),
    /**
     * 按钮
     */
    BUTTON(2),;

    private int resourcesType;

    ResourcesTypeEnum(int resourcesType) {
        this.resourcesType = resourcesType;
    }
}
