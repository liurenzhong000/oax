package com.oax.entity.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/17
 * Time: 11:52
 *
 * 首页 今日统计
 *      订单,交易,充提值 统计
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdersCountVo {

    /**
     * 成交单数
     */
    private int successOrders;

    /**
     * 下单数
     */
    private int countOrders;

    /**
     * 撤单数
     */
    private int countRepealOrders;

    /**
     * 充值次数
     */
    private int countRecharge;

    /**
     * 提币次数
     */
    private int countWithdraw;

}
