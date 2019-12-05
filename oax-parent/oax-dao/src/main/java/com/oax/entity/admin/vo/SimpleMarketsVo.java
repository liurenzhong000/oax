package com.oax.entity.admin.vo;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/11
 * Time: 14:56
 * 简单 所有 市场展示
 */
@Data
public class SimpleMarketsVo {

    private String marketsName;

    private Integer marketId;

    private String coinName;

    private String marketCategoryname;


    public String getMarketsName() {
        return coinName + "/" + marketCategoryname;
    }
}
