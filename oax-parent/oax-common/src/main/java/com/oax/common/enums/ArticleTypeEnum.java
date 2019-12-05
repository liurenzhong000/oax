package com.oax.common.enums;

import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/3
 * Time: 16:02
 * 文章类型 枚举类
 *
 * 1新币上线 2最新公告 3常见问题 4信息公示 5条款说明 6关于我们
 */
@Getter
public enum  ArticleTypeEnum {

    /**
     * 1新币上线
     */
    NEW_UP(1),

    /**
     * 2最新公告
     */
    NEWEST_ANNOUNCEMENT(2),

    /**
     * 3常见问题
     */
    FAQ(3),

    /**
     * 4信息公示
     */
    PUBLICITY(4),

    /**
     * 5条款说明
     */
    CLAUSE(5),

    /**
     * 6关于我们
     */
    ABOUT_US(6)
    ;

    ArticleTypeEnum(int type) {
        this.type = type;
    }

    private int type;



}
