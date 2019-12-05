package com.oax.controller;

import com.github.pagehelper.PageInfo;
import com.oax.common.ResultResponse;
import com.oax.context.HttpContext;
import com.oax.entity.activity.PanicBuyOrder;
import com.oax.service.activity.PanicBuyOrderService;
import com.oax.vo.PanicBuyOrderPostVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * BHB抢购好友 订单
 */
@RestController
@RequestMapping("/panicBuy/order")
@Api(tags = { "PanicBuy API" }, description = "BHB抢购活动的API")
public class PanicBuyOrderController {

    @Autowired
    private PanicBuyOrderService panicBuyOrderService;

    @GetMapping("/buy")
    @ApiOperation("提交订单页数据")
    public ResultResponse buyPage(Integer activityId){
        Integer userId = HttpContext.getUserId();
        PanicBuyOrderPostVo dataVo = panicBuyOrderService.buyPageData(userId, activityId);
        return new ResultResponse(true, dataVo);
    }

    @PostMapping("/buy")
    @ApiOperation("提交订单，进行购买")
    public ResultResponse buy(Integer orderId, String transactionPassword){
        Integer userId = HttpContext.getUserId();
        PanicBuyOrderPostVo dataVo = panicBuyOrderService.buy(userId, orderId, transactionPassword);
        return new ResultResponse(true, "购买成功", dataVo);
    }

    @GetMapping("/list")
    @ApiOperation("订单列表")
    public ResultResponse orderList(@RequestParam(defaultValue = "1") Integer pageNum,@RequestParam(defaultValue = "10") Integer pageSize){
        Integer userId = HttpContext.getUserId();
        PageInfo<PanicBuyOrder> pageInfo = panicBuyOrderService.page(userId, pageNum, pageSize);
        return new ResultResponse(pageInfo);
    }

}
