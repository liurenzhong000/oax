package com.oax.entity.admin.param;

import java.util.Date;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/4
 * Time: 19:47
 * 交易对管理 列表 参数
 */
@Data
public class MarketParam {

    private Integer coinId;

    private Integer categoryId;

    private Date startTime;

    private Date endTime;

}
