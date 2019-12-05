package com.oax.entity.admin.param;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/11
 * Time: 14:45
 * 成交记录 页面参数
 */
@Data
public class TradePageParam extends PageParam {
    private String orderId;

    /**
     * 市场id
     */
    private Integer marketId;

    /**
     * 成交类型 1买进 2卖出
     */
    private Integer orderType;


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


}
