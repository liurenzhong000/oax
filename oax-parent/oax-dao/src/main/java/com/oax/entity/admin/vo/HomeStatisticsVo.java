package com.oax.entity.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/17
 * Time: 11:42
 * 首页 今日统计
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HomeStatisticsVo {

    /**
     * 用户统计
     */
    private  UsersCountVo usersCountVo;

    /**
     * 订单统计
     */
    private OrdersCountVo ordersCountVo;

    /**
     * 从数据库拉取数据时间
     */
    private Date lastUpdateDate;
}
