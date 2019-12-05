package com.oax.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.service.OrdersService;
import com.oax.entity.admin.param.OrderPageParam;
import com.oax.entity.admin.vo.OrdersPageVo;
import com.oax.mapper.front.OrdersMapper;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/11
 * Time: 17:21
 * 委托单 server
 */
@Service
public class OrdersServiceImpl implements OrdersService {
    @Autowired
    private OrdersMapper ordersMapper;

    @Override
    public PageInfo<OrdersPageVo> selectByOrderPageParam(OrderPageParam orderPageParam) {
        PageHelper.startPage(orderPageParam.getPageNum(), orderPageParam.getPageSize());
        List<OrdersPageVo> ordersPageVoList = ordersMapper.selectByOrderPageParam(orderPageParam);
        return new PageInfo<>(ordersPageVoList);
    }
}
