package com.oax.admin.service;

import com.github.pagehelper.PageInfo;
import com.oax.entity.admin.param.OrderPageParam;
import com.oax.entity.admin.vo.OrdersPageVo;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/11
 * Time: 17:20
 * 委托单 server
 */
public interface OrdersService {
    /**
     * 通过orderPageParam 获取委托单
     *
     * @param orderPageParam userId       用户id
     *                       username     用户名
     *                       marketId     市场id
     *                       orderType    委托单类型 类型： 1 买入 2 卖出
     *                       orderStatus  状态-1已撤单 0待撮合 1撮合中 2已完成撮合
     *                       pageNo       页码
     *                       pageSize     一页展示数
     *                       startTime    开始时间
     *                       endTime      结束时间
     * @return
     */
    PageInfo<OrdersPageVo> selectByOrderPageParam(OrderPageParam orderPageParam);
}
