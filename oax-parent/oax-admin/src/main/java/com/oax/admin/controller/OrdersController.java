package com.oax.admin.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.oax.admin.service.OrdersService;
import com.oax.common.PageResultResponse;
import com.oax.common.ResultResponse;
import com.oax.entity.admin.param.OrderPageParam;
import com.oax.entity.admin.vo.OrdersPageVo;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/12
 * Time: 11:37
 * 委托单 控制层
 */
@RestController
@RequestMapping("/orders")
public class OrdersController {


    @Autowired
    private OrdersService ordersService;

    @PostMapping("/page")
    public ResultResponse getAllOrder(@RequestBody OrderPageParam orderPageParam) {

        PageInfo<OrdersPageVo> pageInfo = ordersService.selectByOrderPageParam(orderPageParam);
        PageResultResponse<OrdersPageVo> ordersPageResultResponse = new PageResultResponse<>();

        BeanUtils.copyProperties(pageInfo, ordersPageResultResponse);

        ordersPageResultResponse.setParam(orderPageParam);

        return new ResultResponse(true, ordersPageResultResponse);
    }
}
