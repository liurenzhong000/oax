package com.oax.entity.admin.param;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/11
 * Time: 14:25
 * 委托单页面 参数
 */
@Data
public class OrderPageParam extends PageParam {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 市场id
     */
    private Integer marketId;

    /**
     * 委托单类型
     */
    private Integer orderType;

    /**
     * 委托单状态
     */
    private Integer orderStatus;

    /**
     * 按总量排序
     * 0 正序
     * 1 倒序
     * null 自然序
     */
    private Integer sortTotalPrices;


    /**
     * 按单价
     * 0 正序
     * 1 倒序
     * null 自然序
     */
    private Integer sortPrice;

    /**
     * 按交易数量
     * 0 正序
     * 1 倒序
     * null 自然序
     */
    private Integer sortQty;

    private BigDecimal price;


}
